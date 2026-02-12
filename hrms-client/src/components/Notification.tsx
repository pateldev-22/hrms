import { useEffect, useState } from "react"
import { notificationService } from "@/services/notificationService";
import type { Notifications } from "@/types/notifications";
import { Trash2 } from "lucide-react";
import toast from "react-hot-toast";
export default function Notification(){
    
    const [notifications,setNotifications] = useState<Notifications []>();

    const fetchNotifications = async () => {
        const response = await notificationService.getMyNotifications();
        console.log(response.data);
        setNotifications(response.data);
    }

    useEffect(() => {
        fetchNotifications();
    },[]);

    const markAsReadNotification = async (notificationId : number) => {
        try{
            await notificationService.markAsRead(notificationId);
        }catch(e){
            console.log(e);
            toast.error("Error occured",e);
        }
    }

    const handleClick = (notificationId : number) => {
        markAsReadNotification(notificationId);
    }

    return(
    <>
    <div>
    {notifications && notifications.map((notification) => (
        <ul>
            <div className="flex flex-row justify-between">
                <div>
                    {notification.notificationType}
                </div>
                <div>
                    <button onClick={() => handleClick(notification.notificationId)}>
                        <Trash2></Trash2>
                    </button>
                </div>
            </div>
            <hr/>
            <span className="font-bold">{notification.title}</span> - {notification.message}
            {/* {notification.createdAt} */}
            {/* {notification.isRead} */}
        </ul>
    ))}
    </div>
    </>
    )
}