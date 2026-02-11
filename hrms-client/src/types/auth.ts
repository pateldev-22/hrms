export interface SignupRequest{
    email: string;
    password: string;
    confirmPassword?: string;
    firstName: string;
    lastName: string;
    phone?: string;
    dateOfBirth:Date;
    dateOfJoining:Date;
    profilePhotoUrl:string;
    department:string;
    designation:string;
}