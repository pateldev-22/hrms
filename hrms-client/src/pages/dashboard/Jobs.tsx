import JobCard from "@/components/job/JobCard";
import CreateJobModal from "@/components/job/CreateJobModal";
import { jobService } from "@/services/jobService";
import type { Jobs } from "@/types/jobs";
import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";

export default function Jobs() {
    const [jobs, setJobs] = useState<Jobs[]>([]);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [loading, setLoading] = useState(true);

    const userRole = localStorage.getItem("role");
    console.log(userRole);
    const isHR = JSON.parse(userRole) === 'HR';
    console.log(isHR);

    useEffect(() => {
        fetchJobs();
    }, []);

    const fetchJobs = async () => {
        try {
            setLoading(true);
            const response = await jobService.getJobs();
            setJobs(response.data);
        } catch (error) {
            console.error("Error fetching jobs:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleJobCreated = () => {
        fetchJobs(); 
        setShowCreateModal(false);
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center h-64">
                <div className="text-gray-500">Loading jobs...</div>
            </div>
        );
    }

    return (
        <>
            <div className="flex justify-between items-center mb-6">
                <div>
                    <h1 className="text-2xl font-bold text-gray-900">Job Openings</h1>
                    <p className="text-sm text-gray-500 mt-1">
                        {jobs.length} active position{jobs.length !== 1 ? "s" : ""}
                    </p>
                </div>

                {isHR && (
                    <Button
                        className="bg-green-600 hover:bg-green-700 text-white"
                        onClick={() => setShowCreateModal(true)}
                    >
                        <Plus className="w-4 h-4 mr-2" />
                        Add Job
                    </Button>
                )}
            </div>

            {jobs.length === 0 ? (
                <div className="text-center py-16 text-gray-500">
                    <p className="text-lg">No active job openings</p>
                    {isHR && (
                        <p className="text-sm mt-2">
                            Click "Add Job" to create a new posting
                        </p>
                    )}
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-1 lg:grid-cols-2 gap-6">
                    {jobs.map((job) => (
                        <JobCard key={job.jobId} jobs={job} />
                    ))}
                </div>
            )}

            {isHR && showCreateModal && (
                <CreateJobModal
                    isOpen={showCreateModal}
                    onClose={() => setShowCreateModal(false)}
                    onJobCreated={handleJobCreated}
                />
            )}
        </>
    );
}
