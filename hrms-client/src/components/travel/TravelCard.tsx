import { MapPin, Calendar, Users, FileText } from 'lucide-react';
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import type { TravelPlan } from '@/types/travels';
import { formatDate } from '@/utils/dateFormatter';

interface TravelCardProps {
  travel: TravelPlan;
  onClick: () => void;
}

const TravelCard = ({ travel, onClick }: TravelCardProps) => {
  
  const getStatusText = () => {
    const today = new Date();
    const startDate = new Date(travel.startDate);
    const endDate = new Date(travel.endDate);

    if (today < startDate) return 'Upcoming';
    if (today >= startDate && today <= endDate) return 'Active';
    return 'Completed';
  };

  return (
    <Card
      onClick={onClick}
      className="border border-gray-200 hover:border-green-600 hover:shadow-lg transition-all cursor-pointer"
    >
      <CardHeader className="border-b border-gray-100 pb-3">
        <div className="flex justify-between items-start">
          <CardTitle className="text-lg font-semibold text-gray-900 line-clamp-1">{travel.travelName}</CardTitle>
          <span className={`px-2 py-1 text-xs font-medium`}>{getStatusText()}</span>
        </div>
      </CardHeader>

      <CardContent className="pt-4 space-y-3">
        <div className="flex items-center text-gray-700">
          <MapPin className="w-4 h-4 mr-2 text-gray-400" />
          <span className="text-sm">{travel.destination}</span>
        </div>

        <div className="flex items-center text-gray-700">
          <Calendar className="w-4 h-4 mr-2 text-gray-400" />
          <span className="text-sm">
            {formatDate(travel.startDate)} - {formatDate(travel.endDate)}
          </span>
        </div>

        <div className="flex items-center text-gray-700">
          <Users className="w-4 h-4 mr-2 text-gray-400" />
          <span className="text-sm">{travel.assignments.length} Employees</span>
        </div>

        <div className="flex items-center text-gray-700">
          <FileText className="w-4 h-4 mr-2 text-gray-400" />
          <span className="text-sm">{travel.totalDocuments} Documents</span>
        </div>

        {travel.purpose && (
          <p className="text-sm text-gray-600 line-clamp-2 pt-2 border-t border-gray-100">{travel.purpose}</p>
        )}
      </CardContent>

      <CardFooter className="border-t border-gray-100 pt-3">
        <div className="flex justify-between items-center w-full text-xs text-gray-500">
          <span>Created by {travel.createdByName}</span>
          <span>{formatDate(travel.createdAt)}</span>
        </div>
      </CardFooter>
    </Card>
  );
};

export default TravelCard;
