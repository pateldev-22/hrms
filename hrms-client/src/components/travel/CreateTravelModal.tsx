import { useState, useEffect } from 'react';
import { X, Calendar, MapPin, FileText } from 'lucide-react';
import { travelService } from '@/services/travelService';
import { Button } from '@/components/ui/button';
import type { TravelPlanRequest } from '@/types/travels';
import type { User } from '@/types/User';
import api from '@/services/api';
import toast from 'react-hot-toast';

interface CreateTravelModalProps {
  onClose: () => void;
  onSuccess: () => void;
}

const CreateTravelModal = ({ onClose, onSuccess }: CreateTravelModalProps) => {
  const [formData, setFormData] = useState<TravelPlanRequest>({
    travelName: '',
    destination: '',
    purpose: '',
    startDate: '',
    endDate: '',
    userIds: [],
  });

  const [employees, setEmployees] = useState<User[]>([]);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = async () => {
    try {
      const response = await api.get<User[]>('/user');
      setEmployees(response.data);
    } catch (error) {
      toast.error('Failed to load employees');
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }));
    }
  };

  const handleEmployeeToggle = (userId: number) => {
    setFormData((prev) => ({
      ...prev,
      userIds: prev.userIds.includes(userId) ? prev.userIds.filter((id) => id !== userId) : [...prev.userIds, userId],
    }));
  };

  const validate = () => {
    const newErrors: { [key: string]: string } = {};

    if (!formData.travelName.trim()) newErrors.travelName = 'Travel name is required';
    if (!formData.destination.trim()) newErrors.destination = 'Destination is required';
    if (!formData.startDate) newErrors.startDate = 'Start date is required';
    if (!formData.endDate) newErrors.endDate = 'End date is required';
    if (formData.userIds.length === 0) newErrors.userIds = 'Select at least one employee';

    if (formData.startDate && formData.endDate && new Date(formData.endDate) < new Date(formData.startDate)) {
      newErrors.endDate = 'End date cannot be before start date';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validate()) return;

    setLoading(true);

    try {
      await travelService.createTravelPlan(formData);
      onSuccess();
    } catch (error: any) {
      const message = error.response?.data?.message || 'Failed to create travel plan';
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-opacity-30 bg-black/40 flex items-center justify-center z-50 p-4">
      <div className="bg-green-50 w-full max-w-2xl max-h-[90vh] overflow-hidden flex flex-col border-2 border-black">
        <div className="flex justify-between items-center p-6 border border-black">
          <h2 className="text-xl font-bold text-gray-900">Create Travel Plan</h2>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600 transition-colors">
            <X className="w-6 h-6" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="flex-1 overflow-y-auto p-6 space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Travel Name *</label>
            <div className="relative">
              <FileText className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
              <input
                type="text"
                name="travelName"
                value={formData.travelName}
                onChange={handleChange}
                placeholder="e.g., Client Meeting - Dubai"
                className={`w-full pl-10 pr-3 py-2 border ${
                  errors.travelName ? 'border-red-500' : 'border-gray-300'
                } focus:outline-none focus:ring-2 focus:ring-green-600`}
              />
            </div>
            {errors.travelName && <p className="mt-1 text-sm text-red-600">{errors.travelName}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Destination *</label>
            <div className="relative">
              <MapPin className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
              <input
                type="text"
                name="destination"
                value={formData.destination}
                onChange={handleChange}
                placeholder="e.g., Dubai, UAE"
                className={`w-full pl-10 pr-3 py-2 border ${
                  errors.destination ? 'border-red-500' : 'border-gray-300'
                } focus:outline-none focus:ring-2 focus:ring-green-600`}
              />
            </div>
            {errors.destination && <p className="mt-1 text-sm text-red-600">{errors.destination}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Purpose</label>
            <textarea
              name="purpose"
              value={formData.purpose}
              onChange={handleChange}
              placeholder="Describe the purpose of this travel..."
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 focus:outline-none focus:ring-2 focus:ring-green-600"
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Start Date *</label>
              <div className="relative">
                <Calendar className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
                <input
                  type="date"
                  name="startDate"
                  value={formData.startDate}
                  onChange={handleChange}
                  className={`w-full pl-10 pr-3 py-2 border ${
                    errors.startDate ? 'border-red-500' : 'border-gray-300'
                  } focus:outline-none focus:ring-2 focus:ring-green-600`}
                />
              </div>
              {errors.startDate && <p className="mt-1 text-sm text-red-600">{errors.startDate}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">End Date *</label>
              <div className="relative">
                <Calendar className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
                <input
                  type="date"
                  name="endDate"
                  value={formData.endDate}
                  onChange={handleChange}
                  className={`w-full pl-10 pr-3 py-2 border ${
                    errors.endDate ? 'border-red-500' : 'border-gray-300'
                  } focus:outline-none focus:ring-2 focus:ring-green-600`}
                />
              </div>
              {errors.endDate && <p className="mt-1 text-sm text-red-600">{errors.endDate}</p>}
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Assign Employees *</label>
            <div className="border border-gray-300 max-h-60 overflow-y-auto">
              {employees.map((employee) => (
                <label
                  key={employee.userId}
                  className="flex items-center p-3 hover:bg-gray-50 cursor-pointer border-b border-gray-100 last:border-b-0"
                >
                  <input
                    type="checkbox"
                    checked={formData.userIds.includes(employee.userId)}
                    onChange={() => handleEmployeeToggle(employee.userId)}
                    className="w-4 h-4 text-green-600 border-gray-300 focus:ring-green-600"
                  />
                  <div className="ml-3 flex-1">
                    <p className="text-sm font-medium text-gray-900">
                      {employee.firstName} {employee.lastName}
                    </p>
                    <p className="text-xs text-gray-500">
                      {employee.email} • {employee.department}
                    </p>
                  </div>
                </label>
              ))}
            </div>
            {errors.userIds && <p className="mt-1 text-sm text-red-600">{errors.userIds}</p>}
            <p className="mt-2 text-sm text-gray-500">{formData.userIds.length} employee(s) selected</p>
          </div>
        </form>

        <div className="flex justify-end space-x-3 p-6 border-t border-gray-200">
          <Button
            type="button"
            variant={'destructive'}
            onClick={onClose}
            className="px-4 py-2 border border-gray-300 text-white hover:bg-red-500"
          >
            Cancel
          </Button>
          <Button
            onClick={handleSubmit}
            disabled={loading}
            className="px-4 py-2 bg-green-700 hover:bg-green-800 text-white"
          >
            {loading ? 'Creating...' : 'Create Travel Plan'}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default CreateTravelModal;
