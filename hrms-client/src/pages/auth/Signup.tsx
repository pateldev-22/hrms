import type { SignupRequest } from "@/types/auth";
import { useState } from "react";
import { useNavigate } from "react-router-dom"

export default function Signup(){
    const navigate = useNavigate();
    const [formData,setFormData] = useState<SignupRequest>({
        email:'',
        password:'',
        firstName:'',
        lastName:'',
        phone:'',
        dateOfBirth: new Date,
        dateOfJoining: new Date,
        profilePhotoUrl:'',
        department:'',
        designation:''
    })

    

    return(<>
        
    </>)
}