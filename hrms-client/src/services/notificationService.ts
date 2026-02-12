import api from "./api";

export const notificationService = {
    getMyNotifications : async () => {
        return api.get('/notifications');
    },

    getUnreadNotificationCount : async () => {
        return api.get('/notifications/unread');
    },

    markAsRead : async (id : number) => {
        return api.patch(`/notifications/mark/${id}`);
    },

    getAllNotifications : async () => {
        return api.get('notifications/all');
    }
};