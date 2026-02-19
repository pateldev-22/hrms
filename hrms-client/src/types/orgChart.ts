export interface OrgChartNode {
    userId: number;
    email: string;
    firstName: string;
    lastName: string;
    designation: string;
    department: string;
    profilePhotoUrl: string | null;
    managerId: number | null;
    level?: number;
}

export interface OrgChartChild {
    userId: number;
    email: string;
    firstName: string;
    lastName: string;
    designation: string;
    department: string;
    profilePhotoUrl: string | null;
    managerId: number;
}
