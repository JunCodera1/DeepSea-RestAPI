import type { Profile } from "./profile";

export interface User {
  id: number;
  name: string;
  username: string;
  email: string;
  role: "USER" | "ADMIN";
  avatarUrl: string;
  firstLogin: string;
  lastLogin: string;
  createdAt: string;
  profile: Profile;
}
