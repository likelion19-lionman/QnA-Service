"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { logout } from "@/app/api/auth";

export default function LogoutPage() {
  const router = useRouter();

  useEffect(() => {
    const handleLogout = async () => {
      try {
        await logout();
      } catch (error) {
        alert(error.message);
      } finally {
        router.push("/auth/login");
      }
    };
    handleLogout();
  }, []);

  return <div>로그아웃 중...</div>;
}
