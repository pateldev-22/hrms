import { useState, useEffect } from 'react';
import { useAuth } from '@/context/AuthContext';
import { travelService } from '@/services/travelService';
import type { TravelPlan } from '@/types/travels';
import { Plane, Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import TravelCard from '@/components/travel/TravelCard';
import CreateTravelModal from '@/components/travel/CreateTravelModal';
import toast from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';

const Travels = () => {
  const { user, hasRole } = useAuth();
  const navigate = useNavigate();

  const [travels, setTravels] = useState<TravelPlan[]>([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState<'all' | 'upcoming' | 'active' | 'past'>('all');
  const [showCreateModal, setShowCreateModal] = useState(false);

  const isHR = hasRole('HR');

  useEffect(() => {
    fetchTravels();
  }, [filter]);

  const fetchTravels = async () => {
    try {
      setLoading(true);
      let response;

      switch (filter) {
        case 'upcoming':
          response = await travelService.getUpcomingTravels();
          break;
        case 'active':
          response = await travelService.getActiveTravels();
          break;
        case 'past':
          response = await travelService.getPastTravels();
          break;
        default:
          response = await travelService.getTravels();
      }

      setTravels(response.data);
    } catch (error) {
      toast.error('Failed to load travels');
    } finally {
      setLoading(false);
    }
  };


  const handleTravelCreated = () => {
    setShowCreateModal(false);
    fetchTravels();
    toast.success('Travel plan created successfully!');
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="w-12 h-12 border-4 border-green-200 border-t-green-700 animate-spin"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Travel Management</h1>
          <p className="text-gray-600 mt-1">Manage your travel plans and assignments</p>
        </div>
        {isHR && (
          <Button
            onClick={() => setShowCreateModal(true)}
            className="bg-green-700 hover:bg-green-800 text-white px-4 py-2 font-medium flex items-center"
          >
            <Plus className="w-4 h-4 mr-2" />
            Create Travel Plan
          </Button>
        )}
      </div>

    
      <div className="flex items-center space-x-2">
        <span className="text-sm font-medium text-gray-700">Filter:</span>
        <div className="flex space-x-2">
          {(['all', 'upcoming', 'active', 'past'] as const).map((filterOption) => (
            <button
              key={filterOption}
              onClick={() => setFilter(filterOption)}
              className={`px-4 py-2 text-sm font-medium transition-colors ${
                filter === filterOption
                  ? 'bg-green-700 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              {filterOption.charAt(0).toUpperCase() + filterOption.slice(1)}
            </button>
          ))}
        </div>
      </div>

      {travels.length === 0 ? (
        <Card className="border border-gray-200">
          <CardContent className="py-12 text-center">
            <Plane className="w-16 h-16 text-gray-300 mx-auto mb-4" />
            <h3 className="text-lg font-semibold text-gray-900 mb-2">No travels found</h3>
            <p className="text-gray-600">
              {isHR ? 'Create your first travel plan to get started.' : 'No travel plans assigned to you yet.'}
            </p>
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {travels.map((travel) => (
            <TravelCard key={travel.travelId} travel={travel} onClick={() => navigate(`/travels/${travel.travelId}`)} />
          ))}
        </div>
      )}

      {showCreateModal && (
        <CreateTravelModal onClose={() => setShowCreateModal(false)} onSuccess={handleTravelCreated} />
      )}
    </div>
  );
};

export default Travels;
