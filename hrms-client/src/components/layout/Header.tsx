import { Bell, Search } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { Popover, PopoverContent, PopoverTrigger } from '../ui/popover';
import Notification from '@/components/Notification';
import { useEffect, useState } from 'react';
import { notificationService } from '@/services/notificationService';

const Header = () => {
  const { user } = useAuth();
  const [notificationCount,setNotificationCount] = useState();
  
  const getCount = async () => {
    const response = await notificationService.getUnreadNotificationCount();
    setNotificationCount(response.data);
  }

  useEffect(() => {
    getCount();
  },[notificationCount]);

  return (
    <header className="h-16 bg-zinc-200 border-b border-gray-300 flex items-center justify-between px-6">
      <div className="flex-1 max-w-md">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
          <input
            type="text"
            placeholder="Search..."
            className="w-full pl-10 pr-4 py-2 bg-white border border-gray-500 focus:outline-none focus:ring-2 focus:ring-roima500 focus:border-transparent"
          />
        </div>
      </div>

      <div className="flex items-center space-x-4">
        <button className="relative p-2 text-gray-600 hover:bg-gray-100 rounded-lg transition-colors">
          <Popover>
          <PopoverTrigger asChild>
            <div>
              <Bell className="w-6 h-6" />
              <span className="absolute top-1 right-1 w-4 h-4 bg-red-500 rounded-full"></span>
              <p className="absolute top-[0.5px] left-6 text-white">{notificationCount == 0 ? <></>: notificationCount}</p>
            </div>
          </PopoverTrigger>
          <PopoverContent className='w-80 bg-amber-50 mt-2 mr-6 '>
            <div className="grid gap-4">
              <div className="space-y-2">
                <Notification />
              </div>
            </div>
          </PopoverContent>
          </Popover>
        </button>

        <div className="flex items-center space-x-3">
          <div className="text-right">
            <p className="text-sm font-medium text-gray-700">
              {user?.firstName} {user?.lastName}
            </p>
            <p className="text-xs text-gray-500">{user?.email}</p>
          </div>
          <div className="w-10 h-10 rounded-full bg-roima-500 flex items-center justify-center text-white font-semibold">
            {user?.firstName?.[0]}{user?.lastName?.[0]}
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
