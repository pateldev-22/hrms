export interface Travels {
    travelId : number;
    travelName : string;
    destination : string;
    purpose : string;
    startDate : Date;
    endDate : Date;
    createdByName : string;
    createdByHrId : number;
    assignment : Assignment[];
}

export interface Assignment{
    assignmentId : number;
    employeeId : number;
    employeeName : string;
    employeeEmail : string;
    department : string;
    assignmentStatus : string;
}