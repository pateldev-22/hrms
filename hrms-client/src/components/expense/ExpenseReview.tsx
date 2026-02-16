import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, Eye, CheckCircle, XCircle, Download, FileText } from 'lucide-react';
import { expenseService } from '@/services/expenseService';
import { travelService } from '@/services/travelService';
import type { Expense, ExpenseProof } from '@/types/expenses';
import type { TravelPlan } from '@/types/travels';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import ExpenseReviewModal from '@/components/expense/ExpenseReviewModal';
import { formatDate } from '@/utils/dateFormatter';
import toast from 'react-hot-toast';

const ExpenseReview = () => {
  const { travelId } = useParams<{ travelId: string }>();
  const navigate = useNavigate();

  const [travel, setTravel] = useState<TravelPlan | null>(null);
  const [expenses, setExpenses] = useState<Expense[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedExpense, setSelectedExpense] = useState<Expense | null>(null);
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [expandedExpense, setExpandedExpense] = useState<number | null>(null);
  const [proofs, setProofs] = useState<{ [key: number]: ExpenseProof[] }>({});

  useEffect(() => {
    fetchTravelDetails();
    fetchExpenses();
  }, [travelId]);

  const fetchTravelDetails = async () => {
    try {
      const response = await travelService.getTravelById(Number(travelId));
      setTravel(response.data);
    } catch (error) {
      toast.error('Failed to load travel details');
    }
  };

  const fetchExpenses = async () => {
    try {
      setLoading(true);
      const response = await expenseService.getExpensesByTravel(Number(travelId));
      const submittedExpenses = response.data.filter((exp) => exp.status === 'SUBMITTED');
      setExpenses(submittedExpenses);
    } catch (error) {
      toast.error('Failed to load expenses');
    } finally {
      setLoading(false);
    }
  };

  const fetchProofs = async (expenseId: number) => {
    try {
      const response = await expenseService.getProofs(expenseId);
      setProofs((prev) => ({ ...prev, [expenseId]: response.data }));
    } catch (error) {
      console.error('Failed to load proofs:', error);
    }
  };

  const handleReviewClick = (expense: Expense) => {
    setSelectedExpense(expense);
    setShowReviewModal(true);
  };

  const handleReviewSuccess = () => {
    setShowReviewModal(false);
    setSelectedExpense(null);
    fetchExpenses();
    toast.success('Expense reviewed successfully!');
  };

  const toggleExpand = (expenseId: number) => {
    if (expandedExpense === expenseId) {
      setExpandedExpense(null);
    } else {
      setExpandedExpense(expenseId);
      if (!proofs[expenseId]) {
        fetchProofs(expenseId);
      }
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
      <div className="flex items-center justify-between">
        <div className="flex items-center space-x-4">
          <button onClick={() => navigate('/expenses')} className="text-gray-600 hover:text-gray-900">
            <ArrowLeft className="w-6 h-6" />
          </button>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Review Expenses</h1>
            {travel && (
              <p className="text-gray-600 mt-1">
                {travel.travelName} • {travel.destination}
              </p>
            )}
          </div>
        </div>
      </div>


      {expenses.length === 0 ? (
        <Card className="border border-gray-200">
          <CardContent className="py-12 text-center">
            <CheckCircle className="w-16 h-16 text-gray-300 mx-auto mb-4" />
            <h3 className="text-lg font-semibold text-gray-900 mb-2">No pending expenses</h3>
            <p className="text-gray-600">All expenses for this travel have been reviewed</p>
          </CardContent>
        </Card>
      ) : (
        <Card className="border border-gray-200">
          <CardHeader className="border-b border-gray-100">
            <CardTitle>Pending Expenses for Review</CardTitle>
          </CardHeader>
          <CardContent className="p-0">
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50 border-b border-gray-200">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase">Date</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase">Employee</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase">Category</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase">Description</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-600 uppercase">Amount</th>
                    <th className="px-6 py-3 text-center text-xs font-medium text-gray-600 uppercase"></th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {expenses.map((expense) => (
                    <>
                      <tr key={expense.expenseId} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {formatDate(expense.expenseDate)}
                        </td>
                        <td className="px-6 py-4 text-sm">
                          <div>
                            <p className="font-medium text-gray-900">{expense.employeeName}</p>
                            <p className="text-gray-500 text-xs">{expense.employeeEmail}</p>
                          </div>
                        </td>
                        <td className="px-6 py-4 text-sm text-gray-900">{expense.categoryName}</td>
                        <td className="px-6 py-4 text-sm text-gray-700 max-w-xs">
                          <p className="line-clamp-2">{expense.description}</p>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-semibold text-gray-900">
                          ₹{expense.amount.toLocaleString()}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-center">
                          <div className="flex justify-center space-x-2">
                            <Button
                              onClick={() => toggleExpand(expense.expenseId)}
                              className="bg-blue-600 hover:bg-blue-700 text-white text-xs px-3 py-1"
                            >
                              <Eye className="w-4 h-4 mr-1" />
                              View
                            </Button>
                            <Button
                              onClick={() => handleReviewClick(expense)}
                              className="bg-green-700 hover:bg-green-800 text-white text-xs px-3 py-1"
                            >
                              Review
                            </Button>
                          </div>
                        </td>
                      </tr>

                      {expandedExpense === expense.expenseId && (
                        <tr>
                          <td colSpan={7} className="px-6 py-4 bg-gray-50">
                            <div className="space-y-3">
                              <div>
                                <p className="text-sm font-medium text-gray-900">Full Description:</p>
                                <p className="text-sm text-gray-700 mt-1">{expense.description}</p>
                              </div>

                              <div>
                                <p className="text-sm font-medium text-gray-900 mb-2">Proof Documents:</p>
                                {proofs[expense.expenseId] && proofs[expense.expenseId].length > 0 ? (
                                  <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                                    {proofs[expense.expenseId].map((proof) => (
                                      <div
                                        key={proof.proofId}
                                        className="flex items-center justify-between p-3 border border-gray-200 bg-white"
                                      >
                                        <div className="flex items-center space-x-3 flex-1 min-w-0">
                                          <div className="w-10 h-10 bg-blue-100 flex items-center justify-center">
                                            <FileText className="w-5 h-5 text-blue-600" />
                                          </div>
                                          <div className="flex-1 min-w-0">
                                            <p className="font-medium text-gray-900 truncate text-sm">
                                              {proof.fileName}
                                            </p>
                                            <p className="text-xs text-gray-500">{proof.readableFileSize}</p>
                                          </div>
                                        </div>
                                        <button
                                          onClick={() => window.open(proof.fileUrl, '_blank')}
                                          className="ml-3 p-2 hover:bg-gray-100"
                                          title="Download"
                                        >
                                          <Download className="w-4 h-4 text-gray-600" />
                                        </button>
                                      </div>
                                    ))}
                                  </div>
                                ) : (
                                  <p className="text-sm text-gray-500">Loading proofs...</p>
                                )}
                              </div>
                            </div>
                          </td>
                        </tr>
                      )}
                    </>
                  ))}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      )}

      {showReviewModal && selectedExpense && (
        <ExpenseReviewModal
          expense={selectedExpense}
          onClose={() => setShowReviewModal(false)}
          onSuccess={handleReviewSuccess}
        />
      )}
    </div>
  );
};

export default ExpenseReview;
