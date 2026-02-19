import { useState } from "react";
import { X, UserPlus, Upload } from "lucide-react";
import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { Textarea } from "../ui/textarea";
import { jobService } from "@/services/jobService";
import type { Jobs } from "@/types/jobs";
import toast from "react-hot-toast";

interface ReferFriendModalProps {
    job: Jobs;
    isOpen: boolean;
    onClose: () => void;
}

const ReferFriendModal = ({ job, isOpen, onClose }: ReferFriendModalProps) => {
    const [formData, setFormData] = useState({
        friendName: "",
        friendEmail: "",
        referralNote: ""
    });
    const [cvFile, setCvFile] = useState<File | null>(null);
    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState<any>({});

    const validateForm = () => {
        const newErrors: any = {};

        if (!formData.friendName.trim()) {
            newErrors.friendName = "Friend's name is required";
        }

        if (
            formData.friendEmail &&
            !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.friendEmail)
        ) {
            newErrors.friendEmail = "Invalid email format";
        }

        if (!cvFile) {
            newErrors.cvFile = "CV file is required";
        } else if (!cvFile.name.match(/\.(pdf|doc|docx)$/i)) {
            newErrors.cvFile = "Only PDF, DOC, DOCX files are allowed";
        } else if (cvFile.size > 5 * 1024 * 1024) {
            newErrors.cvFile = "File size must be less than 5MB";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            setCvFile(e.target.files[0]);
            setErrors({ ...errors, cvFile: "" });
        }
    };

    const handleSubmit = async () => {
        if (!validateForm()) return;

        setLoading(true);

        try {
            const referralData = {
                friendName: formData.friendName,
                friendEmail: formData.friendEmail || null,
                referralNote: formData.referralNote || null
            };

            await jobService.referFriend(job.jobId, referralData, cvFile!);

            toast.success(`Referral submitted successfully for ${formData.friendName}!`);

            setFormData({ friendName: "", friendEmail: "", referralNote: "" });
            setCvFile(null);
            onClose();
        } catch (err: any) {
            toast.error(err.response?.data?.message || "Failed to submit referral");
        } finally {
            setLoading(false);
        }
    };

    if (!isOpen) return null;

    return (
        <div
            className="fixed inset-0 bg-black/40 bg-opacity-50 z-50 flex items-center justify-center p-4"
            onClick={(e) => {
                if (e.target === e.currentTarget) onClose();
            }}
        >
            <div className="bg-white shadow-xl w-full max-w-lg flex flex-col max-h-[90vh]">

                <div className="flex justify-between items-center px-6 py-4 border-b shrink-0">
                    <div>
                        <h2 className="text-xl font-semibold text-gray-900">
                            Refer a Friend
                        </h2>
                        <p className="text-sm text-gray-500 mt-0.5">
                            {job.jobTitle} • {job.department}
                        </p>
                    </div>
                    <button
                        onClick={onClose}
                        className="text-gray-400 hover:text-gray-600 hover:bg-gray-100 p-1 transition-colors"
                    >
                        <X className="w-5 h-5" />
                    </button>
                </div>

                <div className="overflow-y-auto flex-1 px-6 py-5 space-y-5">

                    <div className="border p-3">
                        <div className="flex justify-between text-sm text-gray-600">
                            <span>Location: {job.location}</span>
                            <span>Exp: {job.experienceRequired}</span>
                        </div>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1.5">
                            Friend's Name{" "}
                            <span className="text-red-500">*</span>
                        </label>
                        <Input
                            placeholder="John Doe"
                            value={formData.friendName}
                            onChange={(e) =>
                                setFormData({ ...formData, friendName: e.target.value })
                            }
                        />
                        {errors.friendName && (
                            <p className="text-xs text-red-600 mt-1">
                                {errors.friendName}
                            </p>
                        )}
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1.5">
                            Friend's Email{" "}
                            <span className="text-gray-400 text-xs font-normal">
                                (Optional)
                            </span>
                        </label>
                        <Input
                            type="email"
                            placeholder="john.doe@example.com"
                            value={formData.friendEmail}
                            onChange={(e) =>
                                setFormData({ ...formData, friendEmail: e.target.value })
                            }
                        />
                        {errors.friendEmail && (
                            <p className="text-xs text-red-600 mt-1">
                                {errors.friendEmail}
                            </p>
                        )}
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1.5">
                            Upload CV{" "}
                            <span className="text-red-500">*</span>
                        </label>

                        <label
                            htmlFor="cv-upload"
                            className={`flex flex-col items-center justify-center w-full border-2 border-dashed p-5 cursor-pointer transition-colors ${
                                cvFile
                                    ? "border-green-400 bg-green-50"
                                    : "border-gray-300 bg-gray-50 hover:border-green-400 hover:bg-green-50"
                            }`}
                        >
                            <input
                                type="file"
                                id="cv-upload"
                                className="hidden"
                                accept=".pdf,.doc,.docx"
                                onChange={handleFileChange}
                            />

                            {cvFile ? (
                                <div className="text-center">
                                    <div className="text-3xl mb-1">📄</div>
                                    <p className="text-sm font-medium text-green-700">
                                        {cvFile.name}
                                    </p>
                                    <p className="text-xs text-gray-500 mt-0.5">
                                        {(cvFile.size / 1024).toFixed(1)} KB •{" "}
                                        <span className="text-green-600 underline">
                                            Change file
                                        </span>
                                    </p>
                                </div>
                            ) : (
                                <div className="text-center">
                                    <Upload className="w-8 h-8 mx-auto text-gray-400 mb-2" />
                                    <p className="text-sm text-gray-600">
                                        Click to upload CV
                                    </p>
                                    <p className="text-xs text-gray-400 mt-0.5">
                                        PDF, DOC, DOCX — Max 5MB
                                    </p>
                                </div>
                            )}
                        </label>

                        {errors.cvFile && (
                            <p className="text-xs text-red-600 mt-1">
                                {errors.cvFile}
                            </p>
                        )}
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1.5">
                            Note{" "}
                            <span className="text-gray-400 text-xs font-normal">
                                (Optional)
                            </span>
                        </label>
                        <Textarea
                            placeholder="Why do you think your friend is a good fit for this role?"
                            rows={3}
                            value={formData.referralNote}
                            onChange={(e) =>
                                setFormData({ ...formData, referralNote: e.target.value })
                            }
                        />
                    </div>

                    <div className="border p-3">
                        <p className="text-xs">
                            HR and CV reviewers will receive an email with the
                            referral details and attached CV
                        </p>
                    </div>
                </div>

                <div className="flex gap-3 px-6 py-4 border-t bg-gray-50 shrink-0 rounded-b-lg">
                    <Button
                        variant="outline"
                        className="flex-1"
                        onClick={onClose}
                        disabled={loading}
                    >
                        Cancel
                    </Button>
                    <Button
                        className="flex-1 bg-green-600 hover:bg-green-700 text-white"
                        onClick={handleSubmit}
                        disabled={loading}
                    >
                        {loading ? (
                            <span className="flex items-center gap-2">
                                <svg className="animate-spin h-4 w-4" viewBox="0 0 24 24" fill="none">
                                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"/>
                                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8z"/>
                                </svg>
                                Submitting...
                            </span>
                        ) : (
                            <>
                                <UserPlus className="w-4 h-4 mr-2" />
                                Submit Referral
                            </>
                        )}
                    </Button>
                </div>
            </div>
        </div>
    );
};

export default ReferFriendModal;

