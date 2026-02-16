import api from './api';
import type { Expense, ExpenseCategory, ExpenseProof, ExpenseCreateRequest } from '@/types/expenses';

export const expenseService = {
  getExpensesByTravel: (travelId: number) => {
    return api.get<Expense[]>(`/expenses/travel/${travelId}`);
  },

  getExpenseById: (expenseId: number) => {
    return api.get<Expense>(`/expenses/${expenseId}`);
  },

  createExpense: (data: ExpenseCreateRequest) => {
    return api.post<Expense>('/expenses', data);
  },

  submitExpense: (expenseId: number) => {
    return api.put<Expense>(`/expenses/${expenseId}/submit`);
  },

  deleteExpense: (expenseId: number) => {
    return api.delete(`/expenses/${expenseId}`);
  },

  getCategories: () => {
    return api.get<ExpenseCategory[]>('/expense-categories');
  },

  uploadProof: (expenseId: number, file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    return api.post<ExpenseProof>(`/expenses/${expenseId}/proofs`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },

  getProofs: (expenseId: number) => {
    return api.get<ExpenseProof[]>(`/expenses/${expenseId}/proofs`);
  },

  deleteProof: (proofId: number) => {
    return api.delete(`/expenses/proofs/${proofId}`);
  },

  getPendingExpenses: () => {
    return api.get<Expense[]>('/expenses/pending');
  },

  reviewExpense: (expenseId: number, status: 'APPROVED' | 'REJECTED', hrRemarks: string) => {
    return api.put<Expense>(`/expenses/${expenseId}/review`, {
      status,
      hrRemark: hrRemarks,
    });
  },
};
