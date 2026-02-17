export interface Jobs {
    jobId:number;
    jobTitle:string;
    department:string;
    location:string;
    experienceRequired:string;
    jobSummary:string;
    jobDescription:string;
    status:string;
    closingDate:string;
    totalReferrals:number;
    totalShares:number;
    createdAt:Date;
    jdFilePath: string | null;
    hasJD: boolean;
}