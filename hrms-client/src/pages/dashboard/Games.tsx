import { useState, useEffect } from "react";
import { Calendar, Users, Clock, Plus } from "lucide-react";
import { gameService } from "@/services/gameService";
import type { GameConfig, GameSlot } from "@/types/games";
import SlotCard from "@/components/game/SlotCard";
import BookSlotModal from "@/components/game/BookSlotModal";
import { Button } from "@/components/ui/button";

export default function Games() {
    const [configs, setConfigs] = useState<GameConfig[]>([]);
    const [selectedGame, setSelectedGame] = useState<string>("");
    const [selectedDate, setSelectedDate] = useState<string>(
        new Date().toISOString().split('T')[0]
    );
    const [slots, setSlots] = useState<GameSlot[]>([]);
    const [selectedSlot, setSelectedSlot] = useState<GameSlot | null>(null);
    const [showBookModal, setShowBookModal] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchConfigs();
    }, []);

    useEffect(() => {
        if (selectedGame && selectedDate) {
            fetchSlots();
        }
    }, [selectedGame, selectedDate]);

    const fetchConfigs = async () => {
        try {
            const response = await gameService.getConfigs();
            setConfigs(response.data);
            console.log(response.data);
            if (response.data.length > 0) {
                setSelectedGame(response.data[0].gameType);
            }
        } catch (error) {
            console.error("Error fetching configs:", error);
        } finally {
            setLoading(false);
        }
    };

    const fetchSlots = async () => {
        try {
            setLoading(true);
            const response = await gameService.getSlots(selectedGame, selectedDate);
            setSlots(response.data);
        } catch (error) {
            console.error("Error fetching slots:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleBookSlot = (slot: GameSlot) => {
        setSelectedSlot(slot);
        setShowBookModal(true);
    };

    const handleBookingSuccess = () => {
        setShowBookModal(false);
        fetchSlots();
    };

    if (loading && configs.length === 0) {
        return (
            <div className="flex items-center justify-center h-screen">
                <div className="text-gray-500">Loading games...</div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 p-6">
            <div className="max-w-7xl mx-auto">
                <div className="mb-8">
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">
                        Game Slots
                    </h1>
                    <p className="text-gray-600">
                        Book your preferred time slot
                    </p>
                </div>

                <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Select Game
                            </label>
                            <select
                                value={selectedGame}
                                onChange={(e) => setSelectedGame(e.target.value)}
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
                            >
                                {configs.map(config => (
                                    <option key={config.configId} value={config.gameType}>
                                        {config.gameType}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Select Date
                            </label>
                            <input
                                type="date"
                                value={selectedDate}
                                onChange={(e) => setSelectedDate(e.target.value)}
                                min={new Date().toISOString().split('T')[0]}
                                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
                            />
                        </div>
                    </div>
                </div>

                {loading ? (
                    <div className="text-center py-12">
                        <div className="text-gray-500">Loading slots...</div>
                    </div>
                ) : slots.length === 0 ? (
                    <div className="text-center py-12 bg-white rounded-lg shadow-sm">
                        <Calendar className="w-16 h-16 mx-auto text-gray-300 mb-4" />
                        <p className="text-gray-500">No slots available for this date</p>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        {slots.map(slot => (
                            <SlotCard
                                key={slot.slotId}
                                slot={slot}
                                onBook={() => handleBookSlot(slot)}
                            />
                        ))}
                    </div>
                )}
            </div>

            {showBookModal && selectedSlot && (
                <BookSlotModal
                    slot={selectedSlot}
                    isOpen={showBookModal}
                    onClose={() => setShowBookModal(false)}
                    onSuccess={handleBookingSuccess}
                />
            )}
        </div>
    );
}
