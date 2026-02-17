import { useState } from "react";
import { X, Upload, Briefcase } from "lucide-react";
import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { Textarea } from "../ui/textarea";
import { jobService } from "@/services/jobService";
import toast from "react-hot-toast";

interface CreateJobModalProps {
    isOpen: boolean;
    onClose: () => void;
    onJobCreated: () => void;
}

const CreateJobModal = ({ isOpen, onClose, onJobCreated }: CreateJobModalProps) => {
    const [formData, setFormData] = useState({
        jobTitle: "",
        department: "",
        location: "",
        experienceRequired: "",
        jobSummary: "",
        jobDescription: "",
        hrOwnerEmail: "",
        cvReviewerEmails: "",
        closingDate: "",
    });
    const [jdFile, setJdFile] = useState<File | null>(null);
    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState<any>({});

    const handleChange = (
        e: any
    ) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
        if (errors[name]) {
            setErrors({ ...errors, [name]: "" });
        }
    };

    const handleFileChange = (e: any) => {
        if (e.target.files && e.target.files[0]) {
            const file = e.target.files[0];
            if (!file.name.match(/\.(pdf|doc|docx)$/i)) {
                setErrors({ ...errors, jdFile: "Only PDF, DOC, DOCX allowed" });
                return;
            }
            if (file.size > 10 * 1024 * 1024) {
                setErrors({ ...errors, jdFile: "File size must be less than 10MB" });
                return;
            }
            setJdFile(file);
            setErrors({ ...errors, jdFile: "" });
        }
    };

    const validateForm = () => {
        const newErrors: any = {};

        if (!formData.jobTitle.trim()) newErrors.jobTitle = "Job title is required";
        if (!formData.department.trim()) newErrors.department = "Department is required";
        if (!formData.location.trim()) newErrors.location = "Location is required";
        if (!formData.experienceRequired.trim()) newErrors.experienceRequired = "Experience is required";
        if (!formData.jobSummary.trim()) newErrors.jobSummary = "Job summary is required";
        if (!formData.jobDescription.trim()) newErrors.jobDescription = "Job description is required";
        if (!formData.closingDate) newErrors.closingDate = "Closing date is required";

        if (formData.hrOwnerEmail && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.hrOwnerEmail)) {
            newErrors.hrOwnerEmail = "Invalid email format";
        }

        if (formData.closingDate && new Date(formData.closingDate) <= new Date()) {
            newErrors.closingDate = "Closing date must be in the future";
        }
        if(!jdFile){
            newErrors.jdFile = "JD Document is required";
        }else if(jdFile.size > 10*1024*1024){
            newErrors.jdFile = "File size must be less than 10MB";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async () => {
        if (!validateForm()) return;

        setLoading(true);

        try {
            const dto = {
                ...formData,
                closingDate: formData.closingDate, 
            };

            await jobService.createJob(dto, jdFile);
            onJobCreated();
            toast.success("Job Created Sucessfully");
        } catch (err: any) {
            toast.error(err.response?.data?.message || "Failed to create job posting");
        } finally {
            setLoading(false);
        }
    };

    const handleClose = () => {
        setFormData({
            jobTitle: "",
            department: "",
            location: "",
            experienceRequired: "",
            jobSummary: "",
            jobDescription: "",
            hrOwnerEmail: "",
            cvReviewerEmails: "",
            closingDate: "",
        });
        setJdFile(null);
        setErrors({});
        onClose();
    };

    if (!isOpen) return null;

    return (
        <div
            className="fixed inset-0 bg-black/30 bg-opacity-40 z-50 flex items-center justify-center p-4"
            onClick={(e) => {
                if (e.target === e.currentTarget) handleClose();
            }}
        >
            <div className="bg-white border border-black shadow-xl w-full max-w-2xl flex flex-col max-h-[90vh]">

                <div className="flex justify-between items-center px-6 py-4 border-b flex-shrink-0">
                    <div className="flex items-center gap-3">
                        <div className="bg-green-100 p-2 rounded-lg">
                            <Briefcase className="w-5 h-5 text-green-600" />
                        </div>
                        <div>
                            <h2 className="text-xl font-semibold text-gray-900">
                                Create Job Posting
                            </h2>
                            <p className="text-xs text-gray-500">
                                Fill in the details to post a new job
                            </p>
                        </div>
                    </div>
                    <button
                        onClick={handleClose}
                        className="text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-full p-1 transition-colors"
                    >
                        <X className="w-5 h-5" />
                    </button>
                </div>

                <div className="overflow-y-auto flex-1 px-6 py-5 space-y-5">
                    <div>
                        <h3 className="text-sm font-semibold text-gray-700 uppercase tracking-wide mb-3 pb-1 border-b">
                            Basic Information
                        </h3>
                        <div className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                    Job Title <span className="text-red-500">*</span>
                                </label>
                                <Input
                                    name="jobTitle"
                                    placeholder="e.g. Senior Software Engineer"
                                    value={formData.jobTitle}
                                    onChange={handleChange}
                                />
                                {errors.jobTitle && (
                                    <p className="text-xs text-red-600 mt-1">{errors.jobTitle}</p>
                                )}
                            </div>
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                        Department <span className="text-red-500">*</span>
                                    </label>
                                    <Input
                                        name="department"
                                        placeholder="e.g. Engineering"
                                        value={formData.department}
                                        onChange={handleChange}
                                    />
                                    {errors.department && (
                                        <p className="text-xs text-red-600 mt-1">{errors.department}</p>
                                    )}
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                        Location <span className="text-red-500">*</span>
                                    </label>
                                    <Input
                                        name="location"
                                        placeholder="e.g. Mumbai, India"
                                        value={formData.location}
                                        onChange={handleChange}
                                    />
                                    {errors.location && (
                                        <p className="text-xs text-red-600 mt-1">{errors.location}</p>
                                    )}
                                </div>
                            </div>

                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                        Experience Required <span className="text-red-500">*</span>
                                    </label>
                                    <Input
                                        name="experienceRequired"
                                        placeholder="e.g. 3-5 years"
                                        value={formData.experienceRequired}
                                        onChange={handleChange}
                                    />
                                    {errors.experienceRequired && (
                                        <p className="text-xs text-red-600 mt-1">{errors.experienceRequired}</p>
                                    )}
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                        Closing Date <span className="text-red-500">*</span>
                                    </label>
                                    <Input
                                        type="date"
                                        name="closingDate"
                                        value={formData.closingDate}
                                        onChange={handleChange}
                                        min={new Date().toISOString().split("T")[0]}
                                    />
                                    {errors.closingDate && (
                                        <p className="text-xs text-red-600 mt-1">{errors.closingDate}</p>
                                    )}
                                </div>
                            </div>
                        </div>
                    </div>

                    <div>
                        <h3 className="text-sm font-semibold text-gray-700 uppercase tracking-wide mb-3 pb-1 border-b">
                            Job Details
                        </h3>
                        <div className="space-y-4">

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                    Job Summary <span className="text-red-500">*</span>
                                </label>
                                <Textarea
                                    name="jobSummary"
                                    placeholder="Brief summary of the role..."
                                    rows={2}
                                    value={formData.jobSummary}
                                    onChange={handleChange}
                                />
                                {errors.jobSummary && (
                                    <p className="text-xs text-red-600 mt-1">{errors.jobSummary}</p>
                                )}
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                    Job Description <span className="text-red-500">*</span>
                                </label>
                                <Textarea
                                    name="jobDescription"
                                    placeholder="Detailed job description, responsibilities, requirements..."
                                    rows={4}
                                    value={formData.jobDescription}
                                    onChange={handleChange}
                                />
                                {errors.jobDescription && (
                                    <p className="text-xs text-red-600 mt-1">{errors.jobDescription}</p>
                                )}
                            </div>
                        </div>
                    </div>

                    <div>
                        <h3 className="text-sm font-semibold text-gray-700 uppercase tracking-wide mb-3 pb-1 border-b">
                            HR Configuration
                        </h3>
                        <div className="space-y-4">

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                    HR Owner Email{" "}
                                    <span className="text-gray-400 text-xs font-normal">(Optional)</span>
                                </label>
                                <Input
                                    type="email"
                                    name="hrOwnerEmail"
                                    placeholder="hr.owner@company.com"
                                    value={formData.hrOwnerEmail}
                                    onChange={handleChange}
                                />
                                {errors.hrOwnerEmail && (
                                    <p className="text-xs text-red-600 mt-1">{errors.hrOwnerEmail}</p>
                                )}
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1.5">
                                    CV Reviewer Email(s){" "}
                                    <span className="text-gray-400 text-xs font-normal">(Optional)</span>
                                </label>
                                <Input
                                    name="cvReviewerEmails"
                                    placeholder="reviewer1@company.com, reviewer2@company.com"
                                    value={formData.cvReviewerEmails}
                                    onChange={handleChange}
                                />
                                <p className="text-xs text-gray-400 mt-1">
                                    Separate multiple emails with commas
                                </p>
                            </div>
                        </div>
                    </div>

                    <div>
                        <h3 className="text-sm font-semibold text-gray-700 uppercase tracking-wide mb-3 pb-1 border-b">
                            Job Description Document
                        </h3>

                        <label
                            htmlFor="jd-upload"
                            className={`flex flex-col items-center justify-center w-full border-2 border-dashed p-5 cursor-pointer transition-colors ${
                                jdFile
                                    ? "border-green-400 bg-green-50"
                                    : "border-gray-300 bg-gray-50 hover:border-green-400 hover:bg-green-50"
                            }`}
                        >
                            <input
                                type="file"
                                id="jd-upload"
                                className="hidden"
                                accept=".pdf,.doc,.docx"
                                onChange={handleFileChange}
                            />

                            {jdFile ? (
                                <div className="text-center">
                                    <div className="text-3xl mb-1">📄</div>
                                    <p className="text-sm font-medium text-green-700">
                                        {jdFile.name}
                                    </p>
                                    <p className="text-xs text-gray-500 mt-0.5">
                                        {(jdFile.size / 1024).toFixed(1)} KB •{" "}
                                        <span className="text-green-600 underline">Change file</span>
                                    </p>
                                </div>
                            ) : (
                                <div className="text-center">
                                    <Upload className="w-8 h-8 mx-auto text-gray-400 mb-2" />
                                    <p className="text-sm text-gray-600">
                                        Click to upload JD document{" "}
                                        <span className="text-gray-400 text-xs">(Optional)</span>
                                    </p>
                                    <p className="text-xs text-gray-400 mt-0.5">
                                        PDF, DOC, DOCX — Max 10MB
                                    </p>
                                </div>
                            )}
                        </label>

                        {errors.jdFile && (
                            <p className="text-xs text-red-600 mt-1">{errors.jdFile}</p>
                        )}
                    </div>
                </div>

                <div className="flex gap-3 px-6 py-4 border-t bg-gray-50 flex-shrink-0 rounded-b-lg">
                    <Button
                        variant="outline"
                        className="flex-1"
                        onClick={handleClose}
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
                                Creating...
                            </span>
                        ) : (
                            <>
                                <Briefcase className="w-4 h-4 mr-2" />
                                Create Job
                            </>
                        )}
                    </Button>
                </div>
            </div>
        </div>
    );
};

export default CreateJobModal;
