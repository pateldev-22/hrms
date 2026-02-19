import { useState, useEffect } from 'react';
import { Search } from 'lucide-react';
import { orgChartService } from '@/services/orgChartService';
import { useAuth } from '@/context/AuthContext';
import type { OrgChartNode, OrgChartChild } from '@/types/orgChart';
import { Card, CardContent } from '@/components/ui/card';
import OrgChartTree from '@/components/org/OrgChartTree';
import api from '@/services/api';
import toast from 'react-hot-toast';
import { authService } from '@/services/authService';

interface User {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
  designation: string;
  department: string;
}

const OrgChart = () => {
  const { user } = useAuth();
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
  const [hierarchy, setHierarchy] = useState<OrgChartNode[]>([]);
  const [children, setChildren] = useState<OrgChartChild[]>([]);
  const [loading, setLoading] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState<User[]>([]);
  const [allUsers, setAllUsers] = useState<User[]>([]);
  const [currentUser,setCurrentUser] = useState();
  
  useEffect(() => {
        fetchCurrentUser();
        fetchAllUsers();
    }, []);


    const fetchCurrentUser = async () => {
        try{
            setLoading(true);
            const response = await authService.getCurrentUser(user);
            setCurrentUser(response.data);

            fetchOrgChart(response.data.userId)
        }catch(error){
            console.error("error fetching");
        }
    }

  const fetchAllUsers = async () => {
    try {
      const response = await api.get<User[]>('/user');
      setAllUsers(response.data);
    } catch (error) {
      console.error('Failed to load users');
    }
  };

  const fetchOrgChart = async (userId: number) => {
    try {
      setLoading(true);
      setSelectedUserId(userId);

      const hierarchyRes =  await orgChartService.getOrgChart(userId);
      const childrenRes = await orgChartService.getChildren(userId);
      
      setHierarchy(hierarchyRes.data);
      setChildren(childrenRes.data);
    } catch (error) {
      toast.error('Failed to load org chart');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (query: string) => {
    setSearchQuery(query);
    if (query) {
      const filtered = allUsers.filter((u) =>
        `${u.firstName} ${u.designation}`
          .toLowerCase()
          .includes(query.toLowerCase())
      );
      setSearchResults(filtered);
    } else {
      setSearchResults([]);
    }
  };

  const handleEmployeeClick = (userId: number) => {
    fetchOrgChart(userId);
    setSearchQuery('');
    setSearchResults([]);
  };

  if (loading && !hierarchy.length) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="w-12 h-12 border-4 border-green-200 border-t-green-700 animate-spin"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Organization Chart</h1>
        <p className="text-gray-600 mt-1">View company hierarchy</p>
      </div>

      <Card className="border border-gray-200">
        <CardContent className="pt-6">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => handleSearch(e.target.value)}
              placeholder="Search employees..."
              className="w-full pl-10 pr-4 py-3 border border-gray-300 focus:outline-none focus:ring-2 focus:ring-green-600"
            />
            {searchResults.length > 0 && (
              <div className="absolute z-10 w-full mt-2 bg-white border border-gray-300 shadow-lg max-h-64 overflow-y-auto">
                {searchResults.map((result) => (
                  <button
                    key={result.userId}
                    onClick={() => handleEmployeeClick(result.userId)}
                    className="w-full px-4 py-3 hover:bg-gray-50 border-b border-gray-100 text-left"
                  >
                    <p className="font-medium text-gray-900">
                      {result.firstName} {result.lastName}
                    </p>
                    <p className="text-sm text-gray-500">{result.designation}</p>
                  </button>
                ))}
              </div>
            )}
          </div>
        </CardContent>
      </Card>

      {hierarchy.length > 0 && (
        <OrgChartTree
          hierarchy={hierarchy}
          children={children}
          selectedUserId={selectedUserId}
          onEmployeeClick={handleEmployeeClick}
        />
      )}
    </div>
  );
};

export default OrgChart;
