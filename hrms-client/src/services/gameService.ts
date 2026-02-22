import api from "./api";

export const gameService = {
    getConfigs: async () => {
        return api.get('/games/config');
    },

    createConfig: async (data: any) => {
        return api.post('/games/config', data);
    },

    generateSlots: async (gameType: string, date: string) => {
        return api.post(`/games/slots/generate?gameType=${gameType}&date=${date}`);
    },

    getSlots: async (gameType: string, date: string) => {
        return api.get(`/games/slots?gameType=${gameType}&date=${date}`);
    },

    bookSlot: async (data: any) => {
        return api.post('/games/book', data);
    },

    getMyBookings: async () => {
        return api.get('/games/my-bookings');
    },

    cancelBooking: async (bookingId: number) => {
        return api.delete(`/games/bookings/${bookingId}`);
    }
};
