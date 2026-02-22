import { useState, useEffect } from "react";
import { X, UserPlus } from "lucide-react";
import type { GameSlot } from "@/types/games";
import { gameService } from "@/services/gameService";
import { authService } from "@/services/authService";
import { Button } from "../ui/button";
import { useAuth } from "@/context/AuthContext";

interface BookSlotModalProps {
    slot: GameSlot;
    isOpen: boolean;
    onClose: () => void;
    onSuccess: () => void;
}

export default function BookSlotModal({ slot, isOpen, onClose, onSuccess }: BookSlotModalProps) {
    const [allUsers, setAllUsers] = useState<any[]>([]);
    const [selectedParticipants, setSelectedParticipants] = useState<number[]>([]);
    const [loading, setLoading] = useState(false);
    const {user} = useAuth();

    useEffect(() => {
        if (isOpen) {
            fetchUsers();
        }
    }, [isOpen]);

    const fetchUsers = async () => {
        try {
            const response = await authService.getCurrentUser(user);
            const currentUserId = response.data.userId;
            setAllUsers([]);
        } catch (error) {
            console.error("Error fetching users:", error);
        }
    };

    const toggleParticipant = (userId: number) => {
        setSelectedParticipants(prev =>
            prev.includes(userId)
                ? prev.filter(id => id !== userId)
                : [...prev, userId]
        );
    };

    const handleBook = async () => {
        setLoading(true);
        try {
            await gameService.bookSlot({
                slotId: slot.slotId,
                participantUserIds: selectedParticipants.length > 0 ? selectedParticipants : null
            });
            alert("Slot booked successfully!");
            onSuccess();
        } catch (error: any) {
            alert(error.response?.data?.message || "Booking failed");
        } finally {
            setLoading(false);
        }
    };

    if (!isOpen) return null;

    const spotsLeft = slot.maxPlayers - slot.currentPlayers - 1;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
            <div className="bg-white rounded-lg shadow-xl w-full max-w-md">
                <div className="flex justify-between items-center p-6 border-b">
                    <h2 className="text-xl font-semibold text-gray-900">
                        Book Slot
                    </h2>
                    <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
                        <X className="w-5 h-5" />
                    </button>
                </div>

                <div className="p-6">
                    <div className="bg-gray-50 rounded-lg p-4 mb-4">
                        <p className="text-sm text-gray-600">
                            <strong>Time:</strong> {slot.startTime} - {slot.endTime}
                        </p>
                        <p className="text-sm text-gray-600">
                            <strong>Date:</strong> {slot.slotDate}
                        </p>
                        <p className="text-sm text-gray-600">
                            <strong>Game:</strong> {slot.gameType}
                        </p>
                    </div>

                    <div className="mb-4">
                        <p className="text-sm text-gray-700 mb-2">
                            You can add up to {spotsLeft} more participant{spotsLeft !== 1 ? 's' : ''}
                        </p>
                        <p className="text-xs text-gray-500">
                            Selected: {selectedParticipants.length}
                        </p>
                    </div>

                    {spotsLeft > 0 && (
                        <div className="text-sm text-blue-600 mb-4">
                            Add participants feature coming soon
                        </div>
                    )}
                </div>

                <div className="flex gap-3 p-6 border-t bg-gray-50">
                    <Button
                        variant="outline"
                        className="flex-1"
                        onClick={onClose}
                        disabled={loading}
                    >
                        Cancel
                    </Button>
                    <Button
                        className="flex-1 bg-green-600 hover:bg-green-700 text-white"
                        onClick={handleBook}
                        disabled={loading}
                    >
                        {loading ? "Booking..." : "Confirm Booking"}
                    </Button>
                </div>
            </div>
        </div>
    );
}
