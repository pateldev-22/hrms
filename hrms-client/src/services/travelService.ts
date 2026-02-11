import api from "./api";

export const travelService = {
    getTravels: async () => {
        return api.get("/travels");
    }
}