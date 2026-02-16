import { useState, useEffect } from 'react';
import { X, Calendar, DollarSign } from 'lucide-react';
import api from '@/services/api';
import toast from 'react-hot-toast';
import { Button } from '../ui/button';
import type { ExpenseCategory } from '@/types/expenses';

interface CreateExpenseModalProps {
  travelId: number;
  onClose: () => void;
  onSuccess: () => void;
}

const CreateExpenseModal = ({ travelId, onSuccess, onClose }: CreateExpenseModalProps) => {
  const [formData, setFormData] = useState({
    travelId: travelId,
    expenseCategoryName: '',
    amount: '',
    expenseDate: '',
    description: '',
  });

  const [categories, setCategories] = useState<ExpenseCategory[]>([]);
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      const response = await api.get('/expense-categories');
      setCategories(response.data);
    } catch (error) {
      toast.error('Failed to load categories');
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }));
    }
  };

  const validate = () => {
    const newErrors: { [key: string]: string } = {};

    if (!formData.expenseCategoryName) newErrors.expenseCategoryName = 'Category is required';
    if (!formData.amount || Number(formData.amount) <= 0) newErrors.amount = 'Valid amount required';
    if (!formData.expenseDate) newErrors.expenseDate = 'Date is required';
    if (!formData.description.trim()) newErrors.description = 'Description is required';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validate()) return;

    setLoading(true);

    const payload = {
      travelId: formData.travelId,
      expenseCategoryName: formData.expenseCategoryName,
      amount: Number(formData.amount),
      expenseDate: formData.expenseDate,
      description: formData.description,
    };

    try {
      await api.post('/expenses', payload);
      onSuccess();
    } catch (error: any) {
      const message = error.response?.data?.message || 'Failed to create expense';
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/40 bg-opacity-30 border flex items-center justify-center z-50 p-4">
      <div className="bg-white w-full max-w-2xl max-h-[90vh] overflow-hidden flex flex-col">
        <div className="flex justify-between items-center p-6 border-b border-gray-200">
          <h2 className="text-xl font-bold text-gray-900">Create Expense</h2>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600 transition-colors">
            <X className="w-6 h-6" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="flex-1 overflow-y-auto p-6 space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Expense Category *</label>
            <select
              name="expenseCategoryName"
              value={formData.expenseCategoryName}
              onChange={handleChange}
              className={`w-full px-3 py-2 border ${
                errors.expenseCategoryName ? 'border-red-500' : 'border-gray-300'
              } focus:outline-none focus:ring-2 focus:ring-green-600`}
            >
              <option value="">Select Category</option>
              {categories.map((category) => (
                <option key={category.expenseCategoryId} value={category.categoryName}>
                  {category.categoryName}
                  {category.maxAmountPerDay && ` (Max: ₹${category.maxAmountPerDay}/day)`}
                </option>
              ))}
            </select>
            {errors.expenseCategoryName && <p className="mt-1 text-xs text-red-600">{errors.expenseCategoryName}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Amount (₹) *</label>
            <div className="relative">
              <DollarSign className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
              <input
                type="number"
                name="amount"
                min="1"
                step="1"
                value={formData.amount}
                onChange={handleChange}
                placeholder="1000"
                className={`w-full pl-10 pr-3 py-2 border ${
                  errors.amount ? 'border-red-500' : 'border-gray-300'
                } focus:outline-none focus:ring-2 focus:ring-green-600`}
              />
            </div>
            {errors.amount && <p className="mt-1 text-xs text-red-600">{errors.amount}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Expense Date *</label>
            <div className="relative">
              <Calendar className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
              <input
                type="date"
                name="expenseDate"
                value={formData.expenseDate}
                onChange={handleChange}
                className={`w-full pl-10 pr-3 py-2 border ${
                  errors.expenseDate ? 'border-red-500' : 'border-gray-300'
                } focus:outline-none focus:ring-2 focus:ring-green-600`}
              />
            </div>
            {errors.expenseDate && <p className="mt-1 text-xs text-red-600">{errors.expenseDate}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Description *</label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              placeholder="Brief description of the expense..."
              rows={3}
              className={`w-full px-3 py-2 border ${
                errors.description ? 'border-red-500' : 'border-gray-300'
              } focus:outline-none focus:ring-2 focus:ring-green-600`}
            />
            {errors.description && <p className="mt-1 text-xs text-red-600">{errors.description}</p>}
          </div>
        </form>

        <div className="flex justify-end space-x-3 p-6 border-t border-gray-200">
          <Button onClick={onClose} variant="outline" className="border-gray-300 text-gray-700">
            Cancel
          </Button>
          <Button onClick={handleSubmit} disabled={loading} className="bg-green-700 hover:bg-green-800 text-white">
            {loading ? 'Creating...' : 'Create Expense'}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default CreateExpenseModal;

