import api from './api';
import type { TravelPlan, TravelPlanRequest, TravelDocument } from '@/types/travels';

export const travelService = {
  getTravels: async () => {
    return api.get<TravelPlan[]>('/travels');
  },

  getTravelById: async (travelId: number) => {
    return api.get<TravelPlan>(`/travels/${travelId}`);
  },

  getUpcomingTravels: async () => {
    return api.get<TravelPlan[]>('/travels/upcoming');
  },

  getPastTravels: async () => {
    return api.get<TravelPlan[]>('/travels/past');
  },

  getActiveTravels: async () => {
    return api.get<TravelPlan[]>('/travels/active');
  },

  createTravelPlan: async (data: TravelPlanRequest) => {
    return api.post<TravelPlan>('/travels', data);
  },

  updateTravelPlan: async (travelId: number, data: TravelPlanRequest) => {
    return api.put<TravelPlan>(`/travels/${travelId}`, data);
  },

  deleteTravelPlan: async (travelId: number) => {
    return api.delete(`/travels/${travelId}`);
  },

  acknowledgeTravelAssignment: async (travelId: number) => {
    return api.put(`/travels/${travelId}/acknowledge`);
  },

  cancelTravelAssignment: async (travelId: number, employeeId: number) => {
    return api.put(`/travels/${travelId}/cancel/${employeeId}`);
  },

  uploadDocument: async (travelId: number, file: File, documentType: string, userId?: number) => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('documentType', documentType);
    if (userId) {
      formData.append('userId', userId.toString());
    }

    return api.post<TravelDocument>(`/travels/document/${travelId}/documents`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  },

  getTravelDocuments: async (travelId: number) => {
    return api.get<TravelDocument[]>(`/travels/document/${travelId}/documents`);
  },
};
