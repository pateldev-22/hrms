import { useState } from "react";
import { X, Mail, Send } from "lucide-react";
import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { jobService } from "@/services/jobService";
import type { Jobs } from "@/types/jobs";
import toast from "react-hot-toast";

interface ShareJobModalProps {
    job: Jobs;
    isOpen: boolean;
    onClose: () => void;
}

const ShareJobModal = ({ job, isOpen, onClose }: ShareJobModalProps) => {
    const [recipientEmail, setRecipientEmail] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const validateEmail = (email: string) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    };

    const handleShare = async () => {
        setError("");

        if (!recipientEmail.trim()) {
            setError("Please enter an email address");
            return;
        }

        if (!validateEmail(recipientEmail)) {
            setError("Please enter a valid email address");
            return;
        }

        setLoading(true);

        try {
            await jobService.shareJob(job.jobId, { recipientEmail });
            setRecipientEmail("");
            onClose();
            toast.success("Job Shared Sucessfully")
        } catch (err: any) {
            setError(err.response?.data?.message || "Failed to share job");
        } finally {
            setLoading(false);
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
            <div className="bg-white border border-black shadow-xl w-full max-w-md mx-4">
                <div className="flex justify-between items-center p-6 border-b">
                    <h2 className="text-xl font-semibold text-gray-900">Share Job via Email</h2>
                    <button
                        onClick={onClose}
                        className="text-gray-400 hover:text-gray-600"
                    >
                        <X className="w-5 h-5" />
                    </button>
                </div>

                <div className="p-6 space-y-4">
                    <div className="bg-gray-50 p-4 rounded-lg">
                        <h3 className="font-semibold text-gray-900 mb-1">
                            {job.jobTitle}
                        </h3>
                        <p className="text-sm text-gray-600">
                            {job.department} • {job.location}
                        </p>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Recipient Email Address
                        </label>
                        <div className="relative">
                            <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
                            <Input
                                type="email"
                                placeholder="colleague@example.com"
                                value={recipientEmail}
                                onChange={(e) => setRecipientEmail(e.target.value)}
                                className="pl-10"
                            />
                        </div>
                        {error && (
                            <p className="text-sm text-red-600 mt-1">{error}</p>
                        )}
                    </div>

                    <div className="border p-3">
                        <p className="text-sm">
                            An email will be sent with job details and JD attachment
                        </p>
                    </div>
                </div>

                <div className="flex gap-3 p-6 border-t bg-gray-50">
                    <Button
                        variant="outline"
                        className="flex-1"
                        onClick={onClose}
                        disabled={loading}
                    >
                        Cancel
                    </Button>
                    <Button
                        className="flex-1 bg-green-600 hover:bg-green-700"
                        onClick={handleShare}
                        disabled={loading}
                    >
                        {loading ? (
                            "Sending..."
                        ) : (
                            <>
                                <Send className="w-4 h-4 mr-2" />
                                Share Job
                            </>
                        )}
                    </Button>
                </div>
            </div>
        </div>
    );
};

export default ShareJobModal;
