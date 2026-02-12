import { useEffect, useState } from "react"
import { notificationService } from "@/services/notificationService";
export default function Notification(){
    
    const [notifications,setNotifications] = useState();

    const fetchNotifications = async () => {
        const response = await notificationService.getMyNotifications();
        console.log(response.data);
        setNotifications(response.data);
    }

    useEffect(() => {
        fetchNotifications();
    },[]);

    return(
        <>
            Notifications : 
        </>
    )
}