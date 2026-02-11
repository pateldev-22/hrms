import { Card, CardDescription, CardFooter, CardTitle } from "@/components/ui/card";
import { travelService } from "@/services/travelService";
import type { Travels } from "@/types/travels";
import { useEffect, useState } from "react"

export default function Travels(){
    
    const [travels,setTravels] = useState<Travels[]>([]);
    
    const getTravels = async () => {
        try{
        const response = await travelService.getTravels();
        const data = await response.data;
        setTravels(data);
        }catch(e){
            console.log(e);
        }
    }
    console.log(travels);
    useEffect(() => {
        getTravels();
    },[])
    return(<>
        <div>
            {travels.map((travel) => (
                <ul key={travel.travelId} className="grid grid-cols-2 m-3">
                    <Card className="">
                        <CardTitle className="ml-4">
                            {travel.travelName}
                        </CardTitle>
                        <CardDescription className="ml-4">
                            {travel.destination}
                            {travel.purpose}
                        </CardDescription>
                        <CardFooter className="-ml-2.25 flex flex-row justify-between">
                            <div>
                                {travel.startDate} - {travel.endDate}
                            </div>
                            <div>
                                Created By {travel.createdByName}
                            </div>
                            
                        </CardFooter>
                    </Card>

                </ul>
            ))}
        </div>
 
    </>)
}