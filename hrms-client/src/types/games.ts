export interface GameConfig {
    configId: number;
    gameType: string;
    operatingHoursStart: string;
    operatingHoursEnd: string;
    slotDurationMinutes: number;
    maxPlayersPerSlot: number;
    isActive: boolean;
}

export interface GameSlot {
    slotId: number;
    gameType: string;
    slotDate: string;
    startTime: string;
    endTime: string;
    maxPlayers: number;
    currentPlayers: number;
    status: string;
    bookedPlayers: BookedPlayer[];
}

export interface BookedPlayer {
    userId: number;
    fullName: string;
    email: string;
}

export interface Booking {
    bookingId: number;
    slotId: number;
    gameType: string;
    slotDate: string;
    startTime: string;
    endTime: string;
    bookedByName: string;
    participantNames: string[];
    status: string;
    bookedAt: string;
}

