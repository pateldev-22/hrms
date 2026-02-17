import api from "./api";

export const jobService = {
    getJobs: async () => {
        return api.get('/jobs');
    },

    getJobById: async (jobId: number) => {
        return api.get(`/jobs/${jobId}`);
    },

    createJob: async (
        dto: {
            jobTitle: string;
            department: string;
            location: string;
            experienceRequired: string;
            jobSummary: string;
            jobDescription: string;
            hrOwnerEmail: string;
            cvReviewerEmails: string;
            closingDate: string;
        },
        jdFile: File | null
    ) => {
        const formData = new FormData();

        const jsonBlob = new Blob([JSON.stringify(dto)], {
            type: 'application/json'
        });
        formData.append('dto', jsonBlob);

        if (jdFile) {
            formData.append('jdFile', jdFile);
        }

        return api.post('/jobs', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },

    shareJob: async (jobId: number, data: { recipientEmail: string }) => {
        return api.post(`/jobs/${jobId}/share`, data);
    },

    referFriend: async (
        jobId: number,
        referralData: {
            friendName: string;
            friendEmail: string | null;
            referralNote: string | null;
        },
        cvFile: File
    ) => {
        const formData = new FormData();
        const jsonBlob = new Blob([JSON.stringify(referralData)], {
            type: 'application/json'
        });
        formData.append('dto', jsonBlob);
        formData.append('file', cvFile);

        return api.post(`/jobs/${jobId}/refer`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },

    uploadJD: async (jobId: number, file: File) => {
        const formData = new FormData();
        formData.append('file', file);

        return api.post(`/jobs/${jobId}/upload-jd`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    }
};
