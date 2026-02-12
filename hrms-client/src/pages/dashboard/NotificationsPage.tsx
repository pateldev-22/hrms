import { useEffect, useState } from "react"
import { notificationService } from "@/services/notificationService";
import type { Notifications } from "@/types/notifications";

export default function NotificationsPage(){
    
    const [notifications,setNotifications] = useState<Notifications []>();

    const fetchNotifications = async () => {
        const response = await notificationService.getAllNotifications();
        console.log(response.data);
        setNotifications(response.data);
    }

    useEffect(() => {
        fetchNotifications();
    },[]);


    return(
    <>
    <div>
    {notifications && notifications.map((notification) => (
        <ul>
            <div className="flex flex-row justify-between">
                <div>
                    {notification.notificationType}
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