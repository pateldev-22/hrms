export interface Expense {
  expenseId: number;
  travelId: number;
  userId: number;
  categoryId: number;
  reviewedBy?: number;
  amount: number;
  expenseDate: string;
  description: string;
  status: 'DRAFT' | 'SUBMITTED' | 'APPROVED' | 'REJECTED';
  hrRemarks?: string;
  proofCount?: number;

  categoryName?: string;
  employeeName?: string;
  employeeEmail?: string;
  travelName?: string;
}

export interface ExpenseCategory {
  expenseCategoryId: number;
  categoryName: string;
  description?: string;
  maxAmountPerDay?: number;
}

export interface ExpenseProof {
  proofId: number;
  expenseId: number;
  documentId: number;
  fileName: string;
  fileUrl: string;
  fileType: string;
  fileSize: number;
  readableFileSize: string;
  uploadedAt: string;
}

export interface ExpenseCreateRequest {
  travelId: number;
  expenseCategoryName: string;
  amount: number;
  expenseDate: string;
  description: string;
}
