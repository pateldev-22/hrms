import { Clock, Users, Check } from "lucide-react";
import type { GameSlot } from "@/types/games";
import { Button } from "../ui/button";

interface SlotCardProps {
    slot: GameSlot;
    onBook: () => void;
}

export default function SlotCard({ slot, onBook }: SlotCardProps) {
    const isAvailable = slot.status === "AVAILABLE";
    const isFull = slot.status === "FULL";
    const spotsLeft = slot.maxPlayers - slot.currentPlayers;

    return (
        <div className={`
            border-2 rounded-lg p-4 transition-all
            ${isAvailable ? 'border-green-200 bg-white hover:shadow-md' : 'border-gray-200 bg-gray-50'}
        `}>
            <div className="flex justify-between items-start mb-3">
                <div className="flex items-center gap-2">
                    <Clock className="w-5 h-5 text-gray-600" />
                    <span className="font-semibold text-gray-900">
                        {slot.startTime} - {slot.endTime}
                    </span>
                </div>
                <span className={`
                    px-2 py-1 text-xs font-medium rounded-full
                    ${isAvailable ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-700'}
                `}>
                    {slot.status}
                </span>
            </div>

            <div className="flex items-center gap-2 mb-3">
                <Users className="w-4 h-4 text-gray-500" />
                <span className="text-sm text-gray-600">
                    {slot.currentPlayers} / {slot.maxPlayers} players
                </span>
            </div>

            {slot.bookedPlayers.length > 0 && (
                <div className="mb-3">
                    <p className="text-xs text-gray-500 mb-1">Booked by:</p>
                    <div className="flex flex-wrap gap-1">
                        {slot.bookedPlayers.map(player => (
                            <span key={player.userId} className="text-xs bg-gray-100 px-2 py-1 rounded">
                                {player.fullName}
                            </span>
                        ))}
                    </div>
                </div>
            )}

            {isAvailable && (
                <Button
                    onClick={onBook}
                    className="w-full bg-green-600 hover:bg-green-700 text-white"
                    size="sm"
                >
                    Book Slot {spotsLeft > 0 && `(${spotsLeft} spots left)`}
                </Button>
            )}

            {isFull && (
                <div className="text-center text-sm text-gray-500">
                    Slot Full
                </div>
            )}
        </div>
    );
}
