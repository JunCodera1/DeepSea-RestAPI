"use client";

import type React from "react";
import { useState, useEffect, useCallback } from "react";
import {
  Users,
  Shield,
  UserPlus,
  Search,
  BarChart3,
  Trash2,
  Eye,
  Calendar,
  Award,
  AlertCircle,
  RefreshCw,
  ChevronDown,
  Filter,
  Download,
  Menu,
  Bell,
  X,
} from "lucide-react";
import type { SearchFilters, Stats, User } from "../types";
import type { NewAdmin } from "../types/new-admin";
import type { StatCardProps } from "../types/stat-card-props";

const AdminDashboard: React.FC = () => {
  const [activeTab, setActiveTab] = useState<string>("dashboard");
  const [users, setUsers] = useState<User[]>([]);
  const [filteredUsers, setFilteredUsers] = useState<User[]>([]);
  const [stats, setStats] = useState<Stats | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [searchFilters, setSearchFilters] = useState<SearchFilters>({
    username: "",
    email: "",
    role: "",
  });
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [showCreateAdmin, setShowCreateAdmin] = useState<boolean>(false);
  const [showUserDetail, setShowUserDetail] = useState<boolean>(false);
  const [showDeleteConfirm, setShowDeleteConfirm] = useState<boolean>(false);
  const [userToDelete, setUserToDelete] = useState<User | null>(null);
  const [showFilters, setShowFilters] = useState<boolean>(false);
  const [newAdmin, setNewAdmin] = useState<NewAdmin>({
    name: "",
    username: "",
    email: "",
    password: "",
    avatarUrl: "",
  });
  const [isCreatingAdmin, setIsCreatingAdmin] = useState<boolean>(false);
  const [sortConfig, setSortConfig] = useState<{
    key: keyof User | "profile.totalXp";
    direction: "ascending" | "descending";
  } | null>(null);

  // Mock token for demo
  const authToken = "Bearer your-admin-token-here";

  // Enhanced mock data
  const getMockData = useCallback(
    (endpoint: string): User[] | Stats | User | {} => {
      if (endpoint === "/users" || endpoint.startsWith("/users/search")) {
        const allUsers: User[] = [
          {
            id: 1,
            name: "John Doe",
            username: "johndoe",
            email: "john@example.com",
            role: "USER",
            avatarUrl: "/api/placeholder/40/40",
            firstLogin: "2024-01-15",
            lastLogin: "2024-05-22",
            createdAt: "2024-01-10",
            profile: {
              followers: 150,
              following: 200,
              dayStreak: 15,
              totalXp: 2500,
              currentLeague: "Gold",
              topFinishes: 5,
            },
          },
          {
            id: 2,
            name: "Jane Smith",
            username: "janesmith",
            email: "jane@example.com",
            role: "ADMIN",
            avatarUrl: "/api/placeholder/40/40",
            firstLogin: "2024-01-01",
            lastLogin: "2024-05-22",
            createdAt: "2024-01-01",
            profile: {
              followers: 500,
              following: 100,
              dayStreak: 45,
              totalXp: 8900,
              currentLeague: "Diamond",
              topFinishes: 25,
            },
          },
          {
            id: 3,
            name: "Mike Johnson",
            username: "mikej",
            email: "mike@example.com",
            role: "USER",
            avatarUrl: "/api/placeholder/40/40",
            firstLogin: "2024-02-10",
            lastLogin: "2024-05-20",
            createdAt: "2024-02-08",
            profile: {
              followers: 75,
              following: 120,
              dayStreak: 8,
              totalXp: 1200,
              currentLeague: "Silver",
              topFinishes: 2,
            },
          },
          {
            id: 4,
            name: "Sarah Wilson",
            username: "sarahw",
            email: "sarah@example.com",
            role: "USER",
            avatarUrl: "/api/placeholder/40/40",
            firstLogin: "2024-03-01",
            lastLogin: "2024-05-21",
            createdAt: "2024-02-28",
            profile: {
              followers: 320,
              following: 180,
              dayStreak: 22,
              totalXp: 4200,
              currentLeague: "Platinum",
              topFinishes: 12,
            },
          },
          {
            id: 5,
            name: "Tom Anderson",
            username: "toma",
            email: "tom@example.com",
            role: "USER",
            avatarUrl: "/api/placeholder/40/40",
            firstLogin: "2024-04-15",
            lastLogin: "2024-05-19",
            createdAt: "2024-04-10",
            profile: {
              followers: 45,
              following: 90,
              dayStreak: 3,
              totalXp: 650,
              currentLeague: "Bronze",
              topFinishes: 1,
            },
          },
        ];

        const params = new URLSearchParams(endpoint.split("?")[1] || "");
        const username = params.get("username");
        const email = params.get("email");
        const role = params.get("role");

        return allUsers.filter((user: User) => {
          return (
            (!username ||
              user.username.toLowerCase().includes(username.toLowerCase())) &&
            (!email ||
              user.email.toLowerCase().includes(email.toLowerCase())) &&
            (!role || user.role === role)
          );
        });
      } else if (endpoint === "/stats") {
        return {
          totalUsers: 1250,
          adminUsers: 5,
          regularUsers: 1245,
          activeUsersToday: 89,
          newUsersThisWeek: 23,
          totalProfiles: 1180,
        };
      } else if (
        endpoint.startsWith("/users/") &&
        !endpoint.includes("/role")
      ) {
        const userId = Number.parseInt(endpoint.split("/")[2]);
        const allUsers = getMockData("/users") as User[];
        return allUsers.find((user: User) => user.id === userId) || {};
      }
      return {};
    },
    []
  );

  // API call with proper typing and better error handling
  const apiCall = async (
    endpoint: string,
    options: RequestInit = {}
  ): Promise<any> => {
    try {
      setError(null);

      // Simulate API delay for demo
      await new Promise((resolve) => setTimeout(resolve, 800));

      const response = await fetch(`/api/admin${endpoint}`, {
        headers: {
          Authorization: authToken,
          "Content-Type": "application/json",
          ...options.headers,
        },
        ...options,
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      return await response.json();
    } catch (error) {
      console.error("API call failed, using mock data:", error);
      return getMockData(endpoint);
    }
  };

  // Filter users locally for better performance
  const filterUsers = useCallback(() => {
    if (
      !searchFilters.username &&
      !searchFilters.email &&
      !searchFilters.role
    ) {
      setFilteredUsers(users);
      return;
    }

    const filtered = users.filter((user) => {
      const matchesUsername =
        !searchFilters.username ||
        user.username
          .toLowerCase()
          .includes(searchFilters.username.toLowerCase());
      const matchesEmail =
        !searchFilters.email ||
        user.email.toLowerCase().includes(searchFilters.email.toLowerCase());
      const matchesRole =
        !searchFilters.role || user.role === searchFilters.role;

      return matchesUsername && matchesEmail && matchesRole;
    });

    setFilteredUsers(filtered);
  }, [users, searchFilters]);

  // Sort users
  const sortUsers = useCallback(
    (users: User[]) => {
      if (!sortConfig) return users;

      return [...users].sort((a, b) => {
        if (sortConfig.key === "profile.totalXp") {
          const valueA = a.profile?.totalXp || 0;
          const valueB = b.profile?.totalXp || 0;

          if (sortConfig.direction === "ascending") {
            return valueA - valueB;
          }
          return valueB - valueA;
        }

        const valueA = a[sortConfig.key as keyof User];
        const valueB = b[sortConfig.key as keyof User];

        if (valueA < valueB) {
          return sortConfig.direction === "ascending" ? -1 : 1;
        }
        if (valueA > valueB) {
          return sortConfig.direction === "ascending" ? 1 : -1;
        }
        return 0;
      });
    },
    [sortConfig]
  );

  const requestSort = (key: keyof User | "profile.totalXp") => {
    let direction: "ascending" | "descending" = "ascending";

    if (
      sortConfig &&
      sortConfig.key === key &&
      sortConfig.direction === "ascending"
    ) {
      direction = "descending";
    }

    setSortConfig({ key, direction });
  };

  useEffect(() => {
    if (activeTab === "dashboard") {
      loadStats();
    } else if (activeTab === "users") {
      loadUsers();
    }
  }, [activeTab]);

  useEffect(() => {
    filterUsers();
  }, [filterUsers]);

  useEffect(() => {
    if (sortConfig) {
      setFilteredUsers(sortUsers(filteredUsers));
    }
  }, [sortConfig, sortUsers, filteredUsers]);

  const loadUsers = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = (await apiCall("/users")) as User[];
      setUsers(Array.isArray(data) ? data : []);
      setFilteredUsers(Array.isArray(data) ? data : []);
    } catch (error) {
      setError("Failed to load users");
      console.error("Failed to load users:", error);
    } finally {
      setLoading(false);
    }
  }, []);

  const loadStats = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = (await apiCall("/stats")) as Stats;
      setStats(data);
    } catch (error) {
      setError("Failed to load statistics");
      console.error("Failed to load stats:", error);
    } finally {
      setLoading(false);
    }
  }, []);

  const updateUserRole = async (userId: number, newRole: "USER" | "ADMIN") => {
    if (!userId || !newRole) return;

    const user = users.find((u) => u.id === userId);
    if (!user) return;

    if (user.role === newRole) return; // No change needed

    try {
      setError(null);
      await apiCall(`/users/${userId}/role`, {
        method: "PUT",
        body: JSON.stringify({ role: newRole }),
      });

      // Update local state immediately
      setUsers((prevUsers) =>
        prevUsers.map((u) => (u.id === userId ? { ...u, role: newRole } : u))
      );
      setFilteredUsers((prevUsers) =>
        prevUsers.map((u) => (u.id === userId ? { ...u, role: newRole } : u))
      );

      // If the user detail modal is open and this is the selected user, update that too
      if (selectedUser && selectedUser.id === userId) {
        setSelectedUser({ ...selectedUser, role: newRole });
      }
    } catch (error) {
      setError("Failed to update user role");
    }
  };

  const confirmDeleteUser = (user: User) => {
    setUserToDelete(user);
    setShowDeleteConfirm(true);
  };

  const deleteUser = async () => {
    if (!userToDelete) return;

    try {
      setError(null);
      await apiCall(`/users/${userToDelete.id}`, {
        method: "DELETE",
      });

      // Update local state immediately
      setUsers((prevUsers) =>
        prevUsers.filter((u) => u.id !== userToDelete.id)
      );
      setFilteredUsers((prevUsers) =>
        prevUsers.filter((u) => u.id !== userToDelete.id)
      );

      // Close modals
      setShowDeleteConfirm(false);
      if (selectedUser && selectedUser.id === userToDelete.id) {
        setShowUserDetail(false);
      }

      setUserToDelete(null);
    } catch (error) {
      setError("Failed to delete user");
    }
  };

  const createAdmin = async () => {
    if (
      !newAdmin.name.trim() ||
      !newAdmin.username.trim() ||
      !newAdmin.email.trim() ||
      !newAdmin.password.trim()
    ) {
      setError("Please fill in all required fields");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(newAdmin.email)) {
      setError("Please enter a valid email address");
      return;
    }

    if (newAdmin.password.length < 6) {
      setError("Password must be at least 6 characters long");
      return;
    }

    // Check if username or email already exists
    const existingUser = users.find(
      (u) =>
        u.username.toLowerCase() === newAdmin.username.toLowerCase() ||
        u.email.toLowerCase() === newAdmin.email.toLowerCase()
    );

    if (existingUser) {
      setError("Username or email already exists");
      return;
    }

    setIsCreatingAdmin(true);
    try {
      setError(null);
      await apiCall("/create-admin", {
        method: "POST",
        body: JSON.stringify(newAdmin),
      });

      // Add new admin to local state
      const newUser: User = {
        id: Math.max(...users.map((u) => u.id)) + 1,
        name: newAdmin.name,
        username: newAdmin.username,
        email: newAdmin.email,
        role: "ADMIN",
        avatarUrl: newAdmin.avatarUrl || "/api/placeholder/40/40",
        firstLogin: new Date().toISOString().split("T")[0],
        lastLogin: new Date().toISOString().split("T")[0],
        createdAt: new Date().toISOString().split("T")[0],
        profile: {
          followers: 0,
          following: 0,
          dayStreak: 0,
          totalXp: 0,
          currentLeague: "Bronze",
          topFinishes: 0,
        },
      };

      setUsers((prevUsers) => [...prevUsers, newUser]);
      setFilteredUsers((prevUsers) => [...prevUsers, newUser]);

      setShowCreateAdmin(false);
      setNewAdmin({
        name: "",
        username: "",
        email: "",
        password: "",
        avatarUrl: "",
      });
    } catch (error) {
      setError("Failed to create admin user");
    } finally {
      setIsCreatingAdmin(false);
    }
  };

  const viewUserDetail = async (userId: number) => {
    if (!userId) return;

    const user = users.find((u) => u.id === userId);
    if (user) {
      setSelectedUser(user);
      setShowUserDetail(true);
    } else {
      try {
        setError(null);
        const user = (await apiCall(`/users/${userId}`)) as User;
        if (user && user.id) {
          setSelectedUser(user);
          setShowUserDetail(true);
        } else {
          throw new Error("User not found");
        }
      } catch (error) {
        setError("Failed to load user details");
      }
    }
  };

  const clearSearch = () => {
    setSearchFilters({
      username: "",
      email: "",
      role: "",
    });
    setSortConfig(null);
  };

  const refreshData = () => {
    if (activeTab === "dashboard") {
      loadStats();
    } else {
      loadUsers();
    }
  };

  const exportUsers = () => {
    // Create CSV content
    const headers = [
      "ID",
      "Name",
      "Username",
      "Email",
      "Role",
      "Last Login",
      "XP",
    ];
    const csvContent = [
      headers.join(","),
      ...filteredUsers.map((user) =>
        [
          user.id,
          user.name,
          user.username,
          user.email,
          user.role,
          user.lastLogin,
          user.profile?.totalXp || 0,
        ].join(",")
      ),
    ].join("\n");

    // Create download link
    const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.setAttribute("href", url);
    link.setAttribute(
      "download",
      `users_export_${new Date().toISOString().split("T")[0]}.csv`
    );
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  const StatCard: React.FC<StatCardProps> = ({
    title,
    value,
    icon: Icon,
    color,
    change,
  }) => (
    <div className="overflow-hidden transition-all hover:shadow-md bg-white rounded-lg shadow p-6">
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <p className="text-sm font-medium text-gray-500">{title}</p>
          <p className="text-2xl text-gray-700 font-bold">
            {typeof value === "number" ? value.toLocaleString() : value || 0}
          </p>
          {change && (
            <p
              className={`text-xs font-medium flex items-center ${
                change.isPositive ? "text-green-600" : "text-red-600"
              }`}
            >
              {change.isPositive ? "↑" : "↓"} {Math.abs(change.value)}% from
              last month
            </p>
          )}
        </div>
        <div
          className="p-2 rounded-full"
          style={{ backgroundColor: `${color}20` }}
        >
          <Icon className="h-5 w-5" style={{ color }} />
        </div>
      </div>
    </div>
  );

  const UserRow: React.FC<{ user: User }> = ({ user }) => (
    <tr className="border-b hover:bg-gray-50">
      <td className="p-4">
        <div className="flex items-center gap-3">
          <div className="h-9 w-9 rounded-full overflow-hidden border border-gray-200">
            <img
              src={user.avatarUrl || "/api/placeholder/40/40"}
              alt={`${user.name}'s avatar`}
              className="h-full w-full object-cover"
              onError={(e) => {
                const target = e.target as HTMLImageElement;
                target.src = "/api/placeholder/40/40";
              }}
            />
          </div>
          <div>
            <div className="font-medium">{user.name}</div>
            <div className="text-xs text-gray-500">@{user.username}</div>
          </div>
        </div>
      </td>
      <td className="p-4 text-sm">{user.email}</td>
      <td className="p-4">
        <span
          className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
            user.role === "ADMIN"
              ? "bg-red-100 text-red-800"
              : "bg-green-100 text-green-800"
          }`}
        >
          {user.role}
        </span>
      </td>
      <td className="p-4 text-sm">{user.lastLogin}</td>
      <td className="p-4 text-sm">
        <div className="flex items-center gap-2">
          <span>{user.profile?.totalXp?.toLocaleString() || 0}</span>
          <div className="relative w-24 h-2 bg-gray-200 rounded">
            <div
              className="absolute top-0 left-0 h-full bg-blue-500 rounded"
              style={{
                width: `${Math.min(100, (user.profile?.totalXp || 0) / 100)}%`,
              }}
              title={`Level: ${
                Math.floor((user.profile?.totalXp || 0) / 1000) + 1
              }`}
            ></div>
          </div>
        </div>
      </td>
      <td className="p-4 text-right">
        <div className="flex items-center justify-end space-x-2">
          <button
            onClick={() => viewUserDetail(user.id)}
            className="text-blue-600 hover:text-blue-900 p-1 rounded hover:bg-blue-50 transition-colors"
            aria-label={`View details for ${user.name}`}
          >
            <Eye className="h-4 w-4" />
          </button>
          <select
            value={user.role}
            onChange={(e) =>
              updateUserRole(user.id, e.target.value as "USER" | "ADMIN")
            }
            className="text-sm border border-gray-300 rounded px-2 py-1 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            aria-label={`Change role for ${user.name}`}
          >
            <option value="USER">USER</option>
            <option value="ADMIN">ADMIN</option>
          </select>
          <button
            onClick={() => confirmDeleteUser(user)}
            className="text-red-600 hover:text-red-900 p-1 rounded hover:bg-red-50 transition-colors"
            aria-label={`Delete ${user.name}`}
          >
            <Trash2 className="h-4 w-4" />
          </button>
        </div>
      </td>
    </tr>
  );

  const ErrorMessage: React.FC<{ message: string }> = ({ message }) => (
    <div className="bg-red-50 border border-red-200 text-red-800 rounded-md p-4 mb-4">
      <div className="flex">
        <AlertCircle className="h-5 w-5 text-red-500 flex-shrink-0" />
        <div className="ml-3">
          <p className="text-sm">{message}</p>
        </div>
      </div>
    </div>
  );

  const LoadingSkeletons: React.FC = () => (
    <div className="space-y-4">
      {Array.from({ length: 5 }).map((_, i) => (
        <div key={i} className="flex items-center space-x-4">
          <div className="h-12 w-12 rounded-full bg-gray-200 animate-pulse"></div>
          <div className="space-y-2">
            <div className="h-4 w-[250px] bg-gray-200 animate-pulse rounded"></div>
            <div className="h-4 w-[200px] bg-gray-200 animate-pulse rounded"></div>
          </div>
        </div>
      ))}
    </div>
  );

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="sticky top-0 z-30 w-full border-b bg-white shadow-sm">
        <div className="mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex h-16 items-center justify-between">
            <div className="flex items-center gap-2 md:gap-4">
              <button
                className="md:hidden p-2 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-md"
                onClick={() => {
                  // Toggle mobile menu
                }}
              >
                <Menu className="h-5 w-5" />
                <span className="sr-only">Toggle menu</span>
              </button>
              <div className="hidden md:flex items-center gap-2">
                <Shield className="h-6 w-6 text-blue-600" />
                <span className="text-lg text-gray-800 font-bold">
                  Admin Portal
                </span>
              </div>
            </div>

            <div className="flex items-center gap-2">
              <button
                className="p-2 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-md"
                onClick={refreshData}
                disabled={loading}
                aria-label="Refresh data"
              >
                <RefreshCw
                  className={`h-5 w-5 ${loading ? "animate-spin" : ""}`}
                />
              </button>

              <div className="relative">
                <button
                  className="p-2 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-md"
                  aria-label="Notifications"
                >
                  <Bell className="h-5 w-5" />
                </button>
              </div>

              <div className="relative">
                <button
                  className="flex items-center gap-2 p-2 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-md"
                  aria-label="User menu"
                >
                  <div className="h-8 w-8 rounded-full bg-blue-500 flex items-center justify-center">
                    <Shield className="h-4 w-4 text-white" />
                  </div>
                  <span className="hidden md:inline text-sm">Admin</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </header>

      <div className="mx-auto px-4 sm:px-6 lg:px-8 py-6">
        {error && <ErrorMessage message={error} />}

        {/* Main Content */}
        <div className="flex flex-col md:flex-row gap-6">
          {/* Sidebar Navigation - Desktop Only */}
          <aside className="hidden md:flex flex-col w-[240px] shrink-0 border-r pr-6 min-h-[calc(100vh-4rem)]">
            <nav className="grid gap-2 text-sm">
              <button
                className={`flex items-center px-3 py-2 rounded-md ${
                  activeTab === "dashboard"
                    ? "bg-gray-100 text-gray-500 font-medium"
                    : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                }`}
                onClick={() => setActiveTab("dashboard")}
              >
                <BarChart3 className="mr-2 h-4 w-4" />
                Dashboard
              </button>
              <button
                className={`flex items-center px-3 py-2 rounded-md ${
                  activeTab === "users"
                    ? "bg-gray-100 text-gray-900 font-medium"
                    : "text-gray-400 hover:bg-gray-50 hover:text-gray-900"
                }`}
                onClick={() => setActiveTab("users")}
              >
                <Users className="mr-2 h-4 w-4" />
                Users
              </button>
            </nav>
          </aside>

          {/* Mobile Navigation */}
          <div className="md:hidden flex mb-4">
            <div className="w-full bg-white rounded-lg shadow-sm border p-2">
              <div className="flex space-x-2">
                <button
                  className={`flex-1 flex items-center justify-center px-3 py-2 rounded-md ${
                    activeTab === "dashboard"
                      ? "bg-blue-50 text-blue-600 font-medium"
                      : "text-gray-600 hover:bg-gray-50"
                  }`}
                  onClick={() => setActiveTab("dashboard")}
                >
                  <BarChart3 className="mr-2 h-4 w-4" />
                  Dashboard
                </button>
                <button
                  className={`flex-1 flex items-center justify-center px-3 py-2 rounded-md ${
                    activeTab === "users"
                      ? "bg-blue-50 text-blue-600 font-medium"
                      : "text-gray-600 hover:bg-gray-50"
                  }`}
                  onClick={() => setActiveTab("users")}
                >
                  <Users className="mr-2 h-4 w-4" />
                  Users
                </button>
              </div>
            </div>
          </div>

          {/* Main Content Area */}
          <main className="flex-1 space-y-6">
            {/* Dashboard Tab */}
            {activeTab === "dashboard" && (
              <div className="space-y-6">
                <div className="flex items-center justify-between">
                  <h1 className="text-2xl text-gray-700 font-bold tracking-tight">
                    Dashboard
                  </h1>
                  <button
                    className="px-4 py-2 bg-white border border-gray-300 rounded-md text-gray-500 hover:bg-gray-50 flex items-center"
                    onClick={refreshData}
                    disabled={loading}
                  >
                    <RefreshCw
                      className={`mr-2 h-4 w-4 ${
                        loading ? "animate-spin" : ""
                      }`}
                    />
                    Refresh
                  </button>
                </div>

                {loading ? (
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {Array.from({ length: 6 }).map((_, i) => (
                      <div key={i} className="bg-white rounded-lg shadow p-6">
                        <div className="h-4 w-[100px] bg-gray-200 animate-pulse rounded mb-2"></div>
                        <div className="h-8 w-[120px] bg-gray-200 animate-pulse rounded"></div>
                      </div>
                    ))}
                  </div>
                ) : stats ? (
                  <>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                      <StatCard
                        title="Total Users"
                        value={stats.totalUsers}
                        icon={Users}
                        color="#3B82F6"
                        change={{ value: 12, isPositive: true }}
                      />
                      <StatCard
                        title="Admin Users"
                        value={stats.adminUsers}
                        icon={Shield}
                        color="#EF4444"
                      />
                      <StatCard
                        title="Regular Users"
                        value={stats.regularUsers}
                        icon={Users}
                        color="#10B981"
                        change={{ value: 8, isPositive: true }}
                      />
                      <StatCard
                        title="Active Today"
                        value={stats.activeUsersToday}
                        icon={Calendar}
                        color="#F59E0B"
                        change={{ value: 5, isPositive: false }}
                      />
                      <StatCard
                        title="New This Week"
                        value={stats.newUsersThisWeek}
                        icon={UserPlus}
                        color="#8B5CF6"
                        change={{ value: 15, isPositive: true }}
                      />
                      <StatCard
                        title="Total Profiles"
                        value={stats.totalProfiles}
                        icon={Award}
                        color="#06B6D4"
                      />
                    </div>

                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                      <div className="bg-white rounded-lg shadow p-6">
                        <h3 className="text-lg text-gray-700 font-medium mb-4">
                          User Growth
                        </h3>
                        <div className="h-[200px] flex items-end gap-2">
                          {Array.from({ length: 12 }).map((_, i) => {
                            const height = 30 + Math.random() * 70;
                            return (
                              <div
                                key={i}
                                className="flex-1 flex flex-col items-center gap-2"
                              >
                                <div
                                  className="w-full bg-blue-500 rounded-t"
                                  style={{ height: `${height}%` }}
                                ></div>
                                <span className="text-xs text-gray-500">
                                  {new Date(2024, i).toLocaleString("default", {
                                    month: "short",
                                  })}
                                </span>
                              </div>
                            );
                          })}
                        </div>
                      </div>

                      <div className="bg-white rounded-lg shadow p-6">
                        <h3 className="text-lg text-gray-700 font-medium mb-4">
                          User Distribution
                        </h3>
                        <div className="flex items-center justify-center h-[200px]">
                          <div className="relative h-40 w-40">
                            <div className="absolute inset-0 rounded-full border-8 border-blue-100"></div>
                            <div
                              className="absolute inset-0 rounded-full border-8 border-transparent border-t-blue-500 animate-spin"
                              style={{ animationDuration: "3s" }}
                            ></div>
                            <div className="absolute inset-0 text-gray-800 flex items-center justify-center flex-col">
                              <span className="text-2xl font-bold">
                                {stats.totalUsers}
                              </span>
                              <span className="text-xs text-gray-500">
                                Total Users
                              </span>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </>
                ) : null}
              </div>
            )}

            {/* Users Tab */}
            {activeTab === "users" && (
              <div className="space-y-6">
                <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
                  <h1 className="text-2xl font-bold tracking-tight">
                    User Management
                  </h1>
                  <div className="flex items-center gap-2">
                    <button
                      className="px-4 py-2 bg-white border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 flex items-center"
                      onClick={refreshData}
                      disabled={loading}
                    >
                      <RefreshCw
                        className={`mr-2 h-4 w-4 ${
                          loading ? "animate-spin" : ""
                        }`}
                      />
                      Refresh
                    </button>
                    <button
                      className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 flex items-center"
                      onClick={() => setShowCreateAdmin(true)}
                    >
                      <UserPlus className="mr-2 h-4 w-4" />
                      Create Admin
                    </button>
                  </div>
                </div>

                {/* Search and Filters */}
                <div className="bg-white rounded-lg shadow p-6">
                  <div className="flex flex-col gap-4">
                    <div className="flex flex-col sm:flex-row gap-4">
                      <div className="relative flex-1">
                        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
                        <input
                          type="text"
                          placeholder="Search by username..."
                          value={searchFilters.username}
                          onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                            setSearchFilters({
                              ...searchFilters,
                              username: e.target.value,
                            })
                          }
                          className="w-full pl-9 pr-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                        />
                      </div>

                      <div className="flex gap-2">
                        <button
                          className="px-4 py-2 bg-white border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 flex items-center whitespace-nowrap"
                          onClick={() => setShowFilters(!showFilters)}
                        >
                          <Filter className="mr-2 h-4 w-4" />
                          Filters
                          <ChevronDown className="ml-2 h-4 w-4" />
                        </button>

                        <button
                          className="px-4 py-2 bg-white border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 flex items-center whitespace-nowrap"
                          onClick={exportUsers}
                        >
                          <Download className="mr-2 h-4 w-4" />
                          Export
                        </button>
                      </div>
                    </div>

                    {showFilters && (
                      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 pt-2">
                        <div>
                          <label
                            htmlFor="email-filter"
                            className="block text-sm font-medium text-gray-700 mb-1"
                          >
                            Email
                          </label>
                          <input
                            id="email-filter"
                            type="text"
                            placeholder="Filter by email..."
                            value={searchFilters.email}
                            onChange={(
                              e: React.ChangeEvent<HTMLInputElement>
                            ) =>
                              setSearchFilters({
                                ...searchFilters,
                                email: e.target.value,
                              })
                            }
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                          />
                        </div>

                        <div>
                          <label
                            htmlFor="role-filter"
                            className="block text-sm font-medium text-gray-700 mb-1"
                          >
                            Role
                          </label>
                          <select
                            id="role-filter"
                            value={searchFilters.role}
                            onChange={(
                              e: React.ChangeEvent<HTMLSelectElement>
                            ) =>
                              setSearchFilters({
                                ...searchFilters,
                                role: e.target.value,
                              })
                            }
                            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                          >
                            <option value="">All Roles</option>
                            <option value="USER">USER</option>
                            <option value="ADMIN">ADMIN</option>
                          </select>
                        </div>

                        <div className="flex items-end">
                          <button
                            className="w-full px-4 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300"
                            onClick={clearSearch}
                          >
                            Clear Filters
                          </button>
                        </div>
                      </div>
                    )}

                    <div className="text-sm text-gray-500">
                      Showing {filteredUsers.length} of {users.length} users
                    </div>
                  </div>
                </div>

                {/* Users Table */}
                <div className="bg-white rounded-lg shadow overflow-hidden">
                  {loading ? (
                    <div className="p-6">
                      <LoadingSkeletons />
                    </div>
                  ) : (
                    <div className="overflow-x-auto">
                      <table className="w-full">
                        <thead>
                          <tr className="border-b bg-gray-50">
                            <th
                              className="p-4 text-left font-medium text-gray-500 cursor-pointer hover:text-gray-700"
                              onClick={() => requestSort("name")}
                            >
                              User
                              {sortConfig?.key === "name" && (
                                <span className="ml-1">
                                  {sortConfig.direction === "ascending"
                                    ? "↑"
                                    : "↓"}
                                </span>
                              )}
                            </th>
                            <th
                              className="p-4 text-left font-medium text-gray-500 cursor-pointer hover:text-gray-700"
                              onClick={() => requestSort("email")}
                            >
                              Email
                              {sortConfig?.key === "email" && (
                                <span className="ml-1">
                                  {sortConfig.direction === "ascending"
                                    ? "↑"
                                    : "↓"}
                                </span>
                              )}
                            </th>
                            <th
                              className="p-4 text-left font-medium text-gray-500 cursor-pointer hover:text-gray-700"
                              onClick={() => requestSort("role")}
                            >
                              Role
                              {sortConfig?.key === "role" && (
                                <span className="ml-1">
                                  {sortConfig.direction === "ascending"
                                    ? "↑"
                                    : "↓"}
                                </span>
                              )}
                            </th>
                            <th
                              className="p-4 text-left font-medium text-gray-500 cursor-pointer hover:text-gray-700"
                              onClick={() => requestSort("lastLogin")}
                            >
                              Last Login
                              {sortConfig?.key === "lastLogin" && (
                                <span className="ml-1">
                                  {sortConfig.direction === "ascending"
                                    ? "↑"
                                    : "↓"}
                                </span>
                              )}
                            </th>
                            <th
                              className="p-4 text-left font-medium text-gray-500 cursor-pointer hover:text-gray-700"
                              onClick={() => requestSort("profile.totalXp")}
                            >
                              XP
                              {sortConfig?.key === "profile.totalXp" && (
                                <span className="ml-1">
                                  {sortConfig.direction === "ascending"
                                    ? "↑"
                                    : "↓"}
                                </span>
                              )}
                            </th>
                            <th className="p-4 text-right font-medium text-gray-500">
                              Actions
                            </th>
                          </tr>
                        </thead>
                        <tbody>
                          {filteredUsers.length === 0 ? (
                            <tr>
                              <td
                                colSpan={6}
                                className="p-4 text-center text-gray-500"
                              >
                                {users.length === 0
                                  ? "No users found"
                                  : "No users match your search criteria"}
                              </td>
                            </tr>
                          ) : (
                            filteredUsers.map((user) => (
                              <UserRow key={user.id} user={user} />
                            ))
                          )}
                        </tbody>
                      </table>
                    </div>
                  )}
                </div>
              </div>
            )}
          </main>
        </div>
      </div>

      {/* Create Admin Modal */}
      {showCreateAdmin && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg shadow-lg max-w-md w-full">
            <div className="p-6">
              <div className="flex justify-between items-center mb-4">
                <h2 className="text-xl font-semibold">Create New Admin</h2>
                <button
                  className="text-gray-400 hover:text-gray-600 p-1"
                  onClick={() => setShowCreateAdmin(false)}
                  disabled={isCreatingAdmin}
                >
                  <X className="h-5 w-5" />
                </button>
              </div>
              <p className="text-gray-500 mb-4">
                Add a new administrator to the system. They will have full
                access to the admin portal.
              </p>
              <div className="space-y-4">
                <div>
                  <label
                    htmlFor="name"
                    className="block text-sm font-medium text-gray-700 mb-1"
                  >
                    Full Name *
                  </label>
                  <input
                    id="name"
                    type="text"
                    placeholder="Enter full name"
                    value={newAdmin.name}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                      setNewAdmin({ ...newAdmin, name: e.target.value })
                    }
                    disabled={isCreatingAdmin}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
                <div>
                  <label
                    htmlFor="username"
                    className="block text-sm font-medium text-gray-700 mb-1"
                  >
                    Username *
                  </label>
                  <input
                    id="username"
                    type="text"
                    placeholder="Enter username"
                    value={newAdmin.username}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                      setNewAdmin({ ...newAdmin, username: e.target.value })
                    }
                    disabled={isCreatingAdmin}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
                <div>
                  <label
                    htmlFor="email"
                    className="block text-sm font-medium text-gray-700 mb-1"
                  >
                    Email *
                  </label>
                  <input
                    id="email"
                    type="email"
                    placeholder="Enter email address"
                    value={newAdmin.email}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                      setNewAdmin({ ...newAdmin, email: e.target.value })
                    }
                    disabled={isCreatingAdmin}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
                <div>
                  <label
                    htmlFor="password"
                    className="block text-sm font-medium text-gray-700 mb-1"
                  >
                    Password *
                  </label>
                  <input
                    id="password"
                    type="password"
                    placeholder="Enter password (min 6 characters)"
                    value={newAdmin.password}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                      setNewAdmin({ ...newAdmin, password: e.target.value })
                    }
                    disabled={isCreatingAdmin}
                    minLength={6}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
                <div>
                  <label
                    htmlFor="avatar"
                    className="block text-sm font-medium text-gray-700 mb-1"
                  >
                    Avatar URL (optional)
                  </label>
                  <input
                    id="avatar"
                    type="url"
                    placeholder="Enter avatar URL"
                    value={newAdmin.avatarUrl}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                      setNewAdmin({ ...newAdmin, avatarUrl: e.target.value })
                    }
                    disabled={isCreatingAdmin}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
                <p className="text-xs text-gray-500">* Required fields</p>
              </div>
              <div className="flex justify-end space-x-3 mt-6">
                <button
                  className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 border border-gray-300 rounded-md hover:bg-gray-200"
                  onClick={() => setShowCreateAdmin(false)}
                  disabled={isCreatingAdmin}
                >
                  Cancel
                </button>
                <button
                  className="px-4 py-2 text-sm font-medium text-white bg-blue-600 border border-transparent rounded-md hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed flex items-center"
                  onClick={createAdmin}
                  disabled={isCreatingAdmin}
                >
                  {isCreatingAdmin && (
                    <div className="mr-2 h-4 w-4 animate-spin rounded-full border-2 border-white border-t-transparent"></div>
                  )}
                  {isCreatingAdmin ? "Creating..." : "Create Admin"}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* User Detail Modal */}
      {showUserDetail && selectedUser && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg shadow-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <div className="p-6">
              <div className="flex justify-between items-start mb-4">
                <h2 className="text-xl font-semibold">User Details</h2>
                <button
                  className="text-gray-400 hover:text-gray-600 p-1"
                  onClick={() => setShowUserDetail(false)}
                >
                  <X className="h-5 w-5" />
                </button>
              </div>

              <div className="border-b pb-4 mb-4">
                <div className="flex items-center gap-4">
                  <div className="h-16 w-16 rounded-full overflow-hidden border-2 border-gray-200">
                    <img
                      src={selectedUser.avatarUrl || "/api/placeholder/80/80"}
                      alt={`${selectedUser.name}'s avatar`}
                      className="h-full w-full object-cover"
                      onError={(e) => {
                        const target = e.target as HTMLImageElement;
                        target.src = "/api/placeholder/80/80";
                      }}
                    />
                  </div>
                  <div>
                    <h3 className="text-xl font-semibold">
                      {selectedUser.name}
                    </h3>
                    <p className="text-gray-500">@{selectedUser.username}</p>
                    <span
                      className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full mt-2 ${
                        selectedUser.role === "ADMIN"
                          ? "bg-red-100 text-red-800"
                          : "bg-green-100 text-green-800"
                      }`}
                    >
                      {selectedUser.role}
                    </span>
                  </div>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <h3 className="font-medium mb-3 text-gray-900">
                    User Information
                  </h3>
                  <div className="space-y-3">
                    <div>
                      <span className="text-sm font-medium text-gray-500">
                        Email:
                      </span>
                      <p className="text-sm text-gray-900">
                        {selectedUser.email}
                      </p>
                    </div>
                    <div>
                      <span className="text-sm font-medium text-gray-500">
                        Created:
                      </span>
                      <p className="text-sm text-gray-900">
                        {selectedUser.createdAt}
                      </p>
                    </div>
                    <div>
                      <span className="text-sm font-medium text-gray-500">
                        First Login:
                      </span>
                      <p className="text-sm text-gray-900">
                        {selectedUser.firstLogin}
                      </p>
                    </div>
                    <div>
                      <span className="text-sm font-medium text-gray-500">
                        Last Login:
                      </span>
                      <p className="text-sm text-gray-900">
                        {selectedUser.lastLogin}
                      </p>
                    </div>
                  </div>
                </div>

                {selectedUser.profile && (
                  <div>
                    <h3 className="font-medium mb-3 text-gray-900">
                      Profile Statistics
                    </h3>
                    <div className="grid grid-cols-2 gap-4">
                      <div className="bg-blue-50 p-3 rounded-lg">
                        <p className="text-sm font-medium text-blue-600">
                          Followers
                        </p>
                        <p className="text-lg font-bold text-blue-900">
                          {selectedUser.profile.followers.toLocaleString()}
                        </p>
                      </div>
                      <div className="bg-green-50 p-3 rounded-lg">
                        <p className="text-sm font-medium text-green-600">
                          Following
                        </p>
                        <p className="text-lg font-bold text-green-900">
                          {selectedUser.profile.following.toLocaleString()}
                        </p>
                      </div>
                      <div className="bg-yellow-50 p-3 rounded-lg">
                        <p className="text-sm font-medium text-yellow-600">
                          Day Streak
                        </p>
                        <p className="text-lg font-bold text-yellow-900">
                          {selectedUser.profile.dayStreak}
                        </p>
                      </div>
                      <div className="bg-purple-50 p-3 rounded-lg">
                        <p className="text-sm font-medium text-purple-600">
                          Total XP
                        </p>
                        <p className="text-lg font-bold text-purple-900">
                          {selectedUser.profile.totalXp.toLocaleString()}
                        </p>
                      </div>
                      <div className="bg-indigo-50 p-3 rounded-lg">
                        <p className="text-sm font-medium text-indigo-600">
                          League
                        </p>
                        <p className="text-lg font-bold text-indigo-900">
                          {selectedUser.profile.currentLeague}
                        </p>
                      </div>
                      <div className="bg-red-50 p-3 rounded-lg">
                        <p className="text-sm font-medium text-red-600">
                          Top Finishes
                        </p>
                        <p className="text-lg font-bold text-red-900">
                          {selectedUser.profile.topFinishes}
                        </p>
                      </div>
                    </div>
                  </div>
                )}
              </div>

              <div className="mt-6 pt-4 border-t flex justify-end gap-2">
                <button
                  className="px-4 py-2 text-red-600 bg-red-50 border border-red-200 rounded-md hover:bg-red-100 flex items-center"
                  onClick={() => {
                    setShowUserDetail(false);
                    confirmDeleteUser(selectedUser);
                  }}
                >
                  <Trash2 className="mr-2 h-4 w-4" />
                  Delete User
                </button>
                <select
                  value={selectedUser.role}
                  onChange={(e: React.ChangeEvent<HTMLSelectElement>) =>
                    updateUserRole(
                      selectedUser.id,
                      e.target.value as "USER" | "ADMIN"
                    )
                  }
                  className="px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                >
                  <option value="USER">Change to USER</option>
                  <option value="ADMIN">Change to ADMIN</option>
                </select>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Delete Confirmation Modal */}
      {showDeleteConfirm && userToDelete && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg shadow-lg max-w-md w-full">
            <div className="p-6">
              <div className="mb-4">
                <h2 className="text-xl font-semibold">Confirm Deletion</h2>
                <p className="text-gray-500 mt-1">
                  Are you sure you want to delete this user? This action cannot
                  be undone.
                </p>
              </div>

              <div className="flex items-center gap-4 py-4">
                <div className="h-12 w-12 rounded-full overflow-hidden border border-gray-200">
                  <img
                    src={userToDelete.avatarUrl || "/api/placeholder/80/80"}
                    alt={`${userToDelete.name}'s avatar`}
                    className="h-full w-full object-cover"
                    onError={(e) => {
                      const target = e.target as HTMLImageElement;
                      target.src = "/api/placeholder/80/80";
                    }}
                  />
                </div>
                <div>
                  <p className="font-medium">{userToDelete.name}</p>
                  <p className="text-sm text-gray-500">{userToDelete.email}</p>
                </div>
              </div>

              <div className="flex justify-end gap-3 mt-4">
                <button
                  className="px-4 py-2 text-gray-700 bg-gray-100 border border-gray-300 rounded-md hover:bg-gray-200"
                  onClick={() => setShowDeleteConfirm(false)}
                >
                  Cancel
                </button>
                <button
                  className="px-4 py-2 text-white bg-red-600 border border-transparent rounded-md hover:bg-red-700"
                  onClick={deleteUser}
                >
                  Delete User
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
