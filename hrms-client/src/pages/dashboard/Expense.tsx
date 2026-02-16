import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';
import TravelCard from '@/components/travel/TravelCard';
import { travelService } from '@/services/travelService';
import type { TravelPlan } from '@/types/travels';
import toast from 'react-hot-toast';

export default function Expenses() {
  const { hasRole } = useAuth();
  const navigate = useNavigate();
  const [travels, setTravels] = useState<TravelPlan[]>([]);
  const [filter, setFilter] = useState<'active' | 'past'>('active');
  const [loading, setLoading] = useState(true);

  const isHR = hasRole('HR');

  useEffect(() => {
    fetchTravels();
  }, [filter]);

  const fetchTravels = async () => {
    try {
      setLoading(true);
      let response;

      switch (filter) {
        case 'active':
          response = await travelService.getActiveTravels();
          break;
        case 'past':
          response = await travelService.getPastTravels();
          break;
      }

      if (filter === 'past') {
        const tenDaysAgo = new Date();
        tenDaysAgo.setDate(tenDaysAgo.getDate() - 10);

        response.data = response.data.filter((travel) => {
          const endDate = new Date(travel.endDate);
          return endDate >= tenDaysAgo;
        });
      }

      setTravels(response.data);
    } catch (error) {
      toast.error('Failed to load travels');
    } finally {
      setLoading(false);
    }
  };

  const handleTravelClick = (travelId: number) => {
    if (isHR) {
      navigate(`/expenses/review/${travelId}`);
    } else {
      navigate(`/expenses/${travelId}`);
    }
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
      <div>
        <h1 className="text-2xl font-bold text-gray-900">
          {isHR ? 'Expense Review' : 'My Expenses'}
        </h1>
        <p className="text-gray-600 mt-1">
          {isHR
            ? 'Select a travel to review expenses'
            : 'Select a travel to manage expenses'}
        </p>
      </div>

      <div className="flex items-center space-x-2">
        <span className="text-sm font-medium text-gray-700">Filter:</span>
        <div className="flex space-x-2">
          {(['active', 'past'] as const).map((filterOption) => (
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
        <div className="flex flex-col items-center justify-center mt-24">
          <p className="font-bold text-gray-600">No Travels Found</p>
          <p className="text-sm text-gray-500 mt-2">
            {filter === 'past'
              ? 'No travels completed in the last 10 days'
              : 'No active travels at the moment'}
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {travels.map((travel) => (
            <TravelCard
              key={travel.travelId}
              travel={travel}
              onClick={() => handleTravelClick(travel.travelId)}
            />
          ))}
        </div>
      )}
    </div>
  );
}

