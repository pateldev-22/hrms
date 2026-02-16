import { useState } from 'react';
import { X, CheckCircle, XCircle } from 'lucide-react';
import { expenseService } from '@/services/expenseService';
import { Button } from '@/components/ui/button';
import type { Expense } from '@/types/expenses';
import { formatDate } from '@/utils/dateFormatter';
import toast from 'react-hot-toast';

interface ExpenseReviewModalProps {
  expense: Expense;
  onClose: () => void;
  onSuccess: () => void;
}

const ExpenseReviewModal = ({ expense, onClose, onSuccess }: ExpenseReviewModalProps) => {
  const [remarks, setRemarks] = useState('');
  const [loading, setLoading] = useState(false);

  const handleReview = async (status: 'APPROVED' | 'REJECTED') => {
    if (status === 'REJECTED' && !remarks.trim()) {
      toast.error('Remarks are required for rejection');
      return;
    }

    setLoading(true);

    try {
      await expenseService.reviewExpense(expense.expenseId, status, remarks);
      onSuccess();
    } catch (error: any) {
      const message = error.response?.data?.message || 'Failed to review expense';
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/40 bg-opacity-30 flex items-center justify-center z-50 p-4">
      <div className="bg-white w-full max-w-2xl">
        <div className="flex justify-between items-center p-6 border-b border-gray-200">
          <h2 className="text-xl font-bold text-gray-900">Review Expense</h2>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
            <X className="w-6 h-6" />
          </button>
        </div>

        <div className="p-6 space-y-6">
          <div className="bg-gray-50 border border-gray-200 p-4 space-y-2">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <p className="text-xs text-gray-600">Employee</p>
                <p className="text-sm font-medium text-gray-900">{expense.employeeName}</p>
                <p className="text-xs text-gray-500">{expense.employeeEmail}</p>
              </div>
              <div>
                <p className="text-xs text-gray-600">Travel</p>
                <p className="text-sm font-medium text-gray-900">{expense.travelName}</p>
              </div>
              <div>
                <p className="text-xs text-gray-600">Category</p>
                <p className="text-sm font-medium text-gray-900">{expense.categoryName}</p>
              </div>
              <div>
                <p className="text-xs text-gray-600">Date</p>
                <p className="text-sm font-medium text-gray-900">{formatDate(expense.expenseDate)}</p>
              </div>
              <div>
                <p className="text-xs text-gray-600">Amount</p>
                <p className="text-lg font-bold text-green-700">₹{expense.amount.toLocaleString()}</p>
              </div>
              <div>
                <p className="text-xs text-gray-600">Proof Attached</p>
                <p className="text-sm font-medium text-gray-900">{expense.proofCount} document</p>
              </div>
            </div>
            <div>
              <p className="text-xs text-gray-600">Description</p>
              <p className="text-sm text-gray-900 mt-1">{expense.description}</p>
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              HR Remarks {remarks.trim() ? '' : '(Optional for approval, Required for rejection)'}
            </label>
            <textarea
              value={remarks}
              onChange={(e) => setRemarks(e.target.value)}
              placeholder="Add your remarks here..."
              rows={4}
              className="w-full px-3 py-2 border border-gray-300 focus:outline-none focus:ring-2 focus:ring-green-600"
            />
          </div>
        </div>

        <div className="flex justify-end space-x-3 p-6 border-t border-gray-200">
          <Button onClick={onClose} variant="outline" className="border-gray-300 text-gray-700">
            Cancel
          </Button>
          <Button
            onClick={() => handleReview('REJECTED')}
            disabled={loading}
            className="bg-red-600 hover:bg-red-700 text-white"
          >
            <XCircle className="w-4 h-4 mr-2" />
            {loading ? 'Processing...' : 'Reject'}
          </Button>
          <Button
            onClick={() => handleReview('APPROVED')}
            disabled={loading}
            className="bg-green-700 hover:bg-green-800 text-white"
          >
            <CheckCircle className="w-4 h-4 mr-2" />
            {loading ? 'Processing...' : 'Approve'}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default ExpenseReviewModal;
