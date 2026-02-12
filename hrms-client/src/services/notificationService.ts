import api from "./api";

export const notificationService = {
    getMyNotifications : async () => {
        return api.get('/notifications');
    }
};