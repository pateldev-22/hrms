export interface TravelPlan {
  travelId: number;
  travelName: string;
  destination: string;
  purpose?: string;
  startDate: string;
  endDate: string;
  createdByName: string;
  createdByHrId: number;
  assignments: TravelAssignment[];
  totalDocuments: number;
  createdAt: string;
  updatedAt: string;
}

export interface TravelAssignment {
  assignmentId: number;
  employeeId: number;
  employeeName: string;
  employeeEmail: string;
  department: string;
  assignmentStatus: 'ASSIGNED' | 'ACKNOWLEDGED' | 'COMPLETED' | 'CANCELLED';
}

export interface TravelPlanRequest {
  travelName: string;
  destination: string;
  purpose?: string;
  startDate: string;
  endDate: string;
  userIds: number[];
}

export interface TravelDocument {
  travelDocumentId: number;
  travelId: number;
  travelName: string;
  userId: number;
  userName: string;
  uploadedByName: string;
  ownerType: 'HR' | 'EMPLOYEE';
  documentType: string;
  documentId: number;
  fileName: string;
  fileUrl: string;
  fileType: string;
  fileSize: number;
  readableFileSize: string;
  uploadedAt: string;
}
