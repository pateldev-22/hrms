import type { Jobs } from "@/types/jobs";
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "../ui/card";
import { Button } from "../ui/button";
import { Share2, UserPlus } from "lucide-react";
import { useState } from "react";
import ShareJobModal from "./ShareJobModal";
import ReferFriendModal from "./ReferFriendModal";

interface JobCardProps {
    jobs: Jobs;
}

const JobCard = ({ jobs }: JobCardProps) => {
    const [showShareModal, setShowShareModal] = useState(false);
    const [showReferModal, setShowReferModal] = useState(false);

    return (
        <>
            <Card className="border border-gray-200 hover:border-green-600 hover:shadow-lg transition-all">
                <CardHeader className="border-b border-gray-100 pb-3">
                    <div className="flex justify-between items-start">
                        <CardTitle className="text-lg font-semibold text-gray-900 line-clamp-1">
                            {jobs.jobTitle}
                        </CardTitle>
                        <span className="px-3 py-1 text-xs font-medium">
                            {jobs.status}
                        </span>
                    </div>
                </CardHeader>

                <CardContent className="pt-4 space-y-3">
                    <div className="space-y-2">
                        <div className="flex items-center text-gray-700">
                            <span className="text-sm font-medium">Job Location : {jobs.location}</span>
                        </div>

                        <div className="flex items-center text-gray-700">
                            <span className="text-sm">Job Location : {jobs.department}</span>
                        </div>

                        <div className="flex items-center text-gray-700">
                            <span className="text-sm">Experience Required : {jobs.experienceRequired}</span>
                        </div>
                    </div>

                    <p className="text-sm text-gray-600 line-clamp-2 pt-2 border-t border-gray-100">
                        {jobs.jobSummary}
                    </p>

                    <div className="flex justify-between pt-2 border-t border-gray-100">
                        <div className="text-xs text-gray-500">
                            <span>{jobs.totalReferrals} Referrals</span>
                        </div>
                        <div className="text-xs text-gray-500">
                            <span> {jobs.totalShares} Shares</span>
                        </div>
                    </div>
                </CardContent>

                <CardFooter className="border-t border-gray-100 pt-3 flex flex-col gap-3">
                    <div className="flex justify-between items-center w-full text-xs text-gray-500">
                        <span>Closes: {new Date(jobs.closingDate).toLocaleDateString()}</span>
                        <span>Posted: {new Date(jobs.createdAt).toLocaleDateString()}</span>
                    </div>

                    <div className="flex gap-2 w-full">
                        <Button
                            variant="outline"
                            size="sm"
                            className="flex-1 rounded-none border-green-600 text-green-600 hover:bg-green-50"
                            onClick={() => setShowShareModal(true)}
                        >
                            <Share2 className="w-4 h-4 mr-2" />
                            Share Job
                        </Button>

                        <Button
                            size="sm"
                            className="flex-1 rounded-none bg-green-600 hover:bg-green-700 text-white"
                            onClick={() => setShowReferModal(true)}
                        >
                            <UserPlus className="w-4 h-4 mr-2" />
                            Refer Friend
                        </Button>
                    </div>
                </CardFooter>
            </Card>

            {showShareModal && (
                <ShareJobModal
                    job={jobs}
                    isOpen={showShareModal}
                    onClose={() => setShowShareModal(false)}
                />
            )}

            {showReferModal && (
                <ReferFriendModal
                    job={jobs}
                    isOpen={showReferModal}
                    onClose={() => setShowReferModal(false)}
                />
            )}
        </>
    );
};

export default JobCard;
