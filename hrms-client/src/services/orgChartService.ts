import api from "./api";

export const orgChartService = {

    getOrgChart: async (userId: number) => {
        return api.get(`/org-chart/${userId}`);
    },

    getChildren: async (userId: number) => {
        return api.get(`/org-chart/${userId}/child`);
    }
};
