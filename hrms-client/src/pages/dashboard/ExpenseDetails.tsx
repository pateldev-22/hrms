import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, Plus, Trash2, FileText, Download, Send } from 'lucide-react';
import { expenseService } from '@/services/expenseService';
import type { Expense, ExpenseProof } from '@/types/expenses';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import CreateExpenseModal from '@/components/expense/CreateExpenseModal';
import UploadProofModal from '@/components/expense/UploadProofModal';
import { formatDate } from '@/utils/dateFormatter';
import toast from 'react-hot-toast';

const ExpenseDetails = () => {
  const { travelId } = useParams<{ travelId: string }>();
  const navigate = useNavigate();

  const [expenses, setExpenses] = useState<Expense[]>([]);
  const [loading, setLoading] = useState(true);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showProofModal, setShowProofModal] = useState(false);
  const [selectedExpense, setSelectedExpense] = useState<Expense | null>(null);
  const [expandedExpense, setExpandedExpense] = useState<number | null>(null);
  const [proofs, setProofs] = useState<{ [key: number]: ExpenseProof[] }>({});

  useEffect(() => {
    fetchExpenses();
  }, [travelId]);

  const fetchExpenses = async () => {
    try {
      setLoading(true);
      const response = await expenseService.getExpensesByTravel(Number(travelId));
      setExpenses(response.data);
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

  const handleExpenseCreated = () => {
    setShowCreateModal(false);
    fetchExpenses();
    toast.success('Expense created successfully!');
  };

  const handleProofUploaded = () => {
    setShowProofModal(false);
    if (selectedExpense) {
      fetchProofs(selectedExpense.expenseId);
      fetchExpenses();
    }
    toast.success('Proof uploaded successfully!');
  };

  const handleSubmitExpense = async (expense: Expense) => {
    if (!proofs[expense.expenseId] || proofs[expense.expenseId].length === 0) {
      toast.error('Please upload at least one proof document before submitting');
      return;
    }
    
    try {
        await expenseService.submitExpense(expense.expenseId);
        toast.success('Expense submitted for review!');
        fetchExpenses();
    } catch (error: any) {
        const message = error.response?.data?.message || 'Failed to submit expense';
        toast.error(message);
    }
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
            <h1 className="text-2xl font-bold text-gray-900">Travel Expenses</h1>
            <p className="text-gray-600 mt-1">
            </p>
          </div>
        </div>

        <Button onClick={() => setShowCreateModal(true)} className="bg-green-700 hover:bg-green-800 text-white">
          <Plus className="w-4 h-4 mr-2" />
          Add Expense
        </Button>
      </div>

      {expenses.length === 0 ? (
        <Card className="border border-gray-200">
          <CardContent className="py-12 text-center">
            <FileText className="w-16 h-16 text-gray-300 mx-auto mb-4" />
            <h3 className="text-lg font-semibold text-gray-900 mb-2">No expenses yet</h3>
            <p className="text-gray-600">Start by adding your first expense for this travel.</p>
          </CardContent>
        </Card>
      ) : (
        <div className="space-y-4">
          {expenses.map((expense) => (
            <Card key={expense.expenseId} className="border border-gray-200">
              <CardHeader className="border-b border-gray-100 pb-4">
                <div className="flex justify-between items-start">
                  <div className="flex-1">
                    <div className="flex items-center space-x-3">
                      <CardTitle className="text-lg">{expense.description}</CardTitle>
                      <span className={`px-2 py-1 text-xs font-medium`}>
                        {expense.status}
                      </span>
                    </div>
                    <div className="mt-2 flex items-center space-x-4 text-sm text-gray-600">
                      <span>{formatDate(expense.expenseDate)}</span>
                      <span>•</span>
                      <span className="font-semibold text-gray-900">₹{expense.amount.toLocaleString()}</span>
                      <span>•</span>
                      <span>{proofs[expense.expenseId]?.length || 0} proof(s)</span>
                    </div>
                  </div>

                  <div className="flex items-center space-x-2">
                    {expense.status === 'DRAFT' && (
                      <>
                        <Button
                          onClick={() => {
                            setSelectedExpense(expense);
                            setShowProofModal(true);
                          }}
                          variant={'outline'}
                          className="text-black text-sm"
                        >
                          Upload Proof
                        </Button>
                        <Button
                          onClick={() => handleSubmitExpense(expense)}
                          className="bg-green-700 hover:bg-green-800 text-white text-sm"
                        >
                          <Send className="w-4 h-4 mr-1" />
                          Submit
                        </Button>
                    </>
                    )}
                    <button
                      onClick={() => toggleExpand(expense.expenseId)}
                      className="text-sm text-gray-600 hover:text-gray-900"
                    >
                      {expandedExpense === expense.expenseId ? 'Hide Details' : 'Show Details'}
                    </button>
                  </div>
                </div>
              </CardHeader>

              {expandedExpense === expense.expenseId && (
                <CardContent className="pt-4">
                  {expense.hrRemarks && (
                    <div className="mb-4 p-3 bg-yellow-50 border border-yellow-200">
                      <p className="text-sm font-medium text-gray-900">HR Remarks:</p>
                      <p className="text-sm text-gray-700 mt-1">{expense.hrRemarks}</p>
                    </div>
                  )}

                  <h4 className="font-medium text-gray-900 mb-3">Proof Documents</h4>
                  {proofs[expense.expenseId] && proofs[expense.expenseId].length > 0 ? (
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                      {proofs[expense.expenseId].map((proof) => (
                        <div
                          key={proof.proofId}
                          className="flex items-center justify-between p-3 border border-gray-200 hover:border-green-600 transition-colors"
                        >
                          <div className="flex items-center space-x-3 flex-1 min-w-0">
                            <div className="w-10 h-10 bg-blue-100 flex items-center justify-center">
                              <FileText className="w-5 h-5 text-blue-600" />
                            </div>
                            <div className="flex-1 min-w-0">
                              <p className="font-medium text-gray-900 truncate text-sm">{proof.fileName}</p>
                              <p className="text-xs text-gray-500">{proof.readableFileSize}</p>
                            </div>
                          </div>
                          <div className="flex items-center space-x-2 ml-2">
                            <button
                              onClick={() => window.open(proof.fileUrl, '_blank')}
                              className="p-2 hover:bg-gray-100"
                              title="Download"
                            >
                              <Download className="w-4 h-4 text-gray-600" />
                            </button>
                          </div>
                        </div>
                      ))}
                    </div>
                  ) : (
                    <p className="text-sm text-gray-500">No proofs uploaded yet</p>
                  )}
                </CardContent>
              )}
            </Card>
          ))}
        </div>
      )}

      {showCreateModal && (
        <CreateExpenseModal
          travelId={Number(travelId)}
          onClose={() => setShowCreateModal(false)}
          onSuccess={handleExpenseCreated}
        />
      )}

      {showProofModal && selectedExpense && (
        <UploadProofModal
          expenseId={selectedExpense.expenseId}
          onClose={() => {
            setShowProofModal(false);
            setSelectedExpense(null);
          }}
          onSuccess={handleProofUploaded}
        />
      )}
    </div>
  );
};

export default ExpenseDetails;
