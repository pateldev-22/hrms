
import { NavLink, useNavigate } from 'react-router-dom';
import { 
  LogOut
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useEffect, useState } from 'react';
import { authService } from '@/services/authService';

const Sidebar = () => {
  const { logout, user } = useAuth();
  const navigate = useNavigate();

  const [userData,setUserData] = useState();

  const getUser = async() => {
    const response = await authService.getCurrentUser(user);
    setUserData(response.data);
}

  useEffect(() => {
    getUser();
  },[])

  const handleLogout = () => {
    console.log("button click thayu");
    navigate("/login");
    logout();
  }

  const navigation = [
    { name: 'Dashboard', href: '/' },
    { name: 'Travels', href: '/travels'},
    { name: 'Expenses', href: '/expenses'},
  ];

  return (
    <div className="w-64 bg-roima600 text-white flex flex-col">
      <div className="h-16 flex items-center px-6 bg-roima700">
        <div className="flex items-center space-x-3">
          <span className="text-xl font-bold">ROIMA INT.</span>
        </div>
      </div>

      <nav className="flex-1 px-4 py-6 space-y-1 overflow-y-auto custom-scrollbar">
        {navigation.map((item) => (
          <NavLink
            key={item.name}
            to={item.href}
            end={item.href === '/'}
            className={({ isActive }) =>
              `flex items-center px-4 py-3 text-sm font-medium transition-colors ${
                isActive
                  ? 'bg-roima500 text-white'
                  : 'text-roima100 hover:bg-roima500/50 hover:text-white'
              }`
            }
          >
            {item.name}
          </NavLink>
        ))}
      </nav>

      <div className="p-4 border-t border-roima500">
        <div className="flex items-center mb-3 px-2">
          <div className="w-8 h-8 rounded-full bg-roima400 flex items-center justify-center text-sm font-semibold">
            {userData?.firstName?.[0]}{userData?.lastName?.[0]}
          </div>
          <div className="ml-3 flex-1 min-w-0">
            <p className="text-sm font-medium truncate">
              {userData?.firstName} {userData?.lastName}
            </p>
            <p className="text-xs text-roima200 truncate">{userData?.role}</p>
          </div>
        </div>
        <button
          onClick={handleLogout}
          className="w-full flex items-center px-4 py-2 text-sm font-medium text-roima100 hover:bg-roima500/50 hover:text-white rounded-lg transition-colors"
        >
          <LogOut className="w-5 h-5 mr-3" />
          Logout
        </button>
      </div>
    </div>
  );
};

export default Sidebar