"use client";

import { useRouter } from "next/navigation";
import { logout } from "@/app/api/auth";

export default function HomePage() {
  const router = useRouter();

  const handleLogout = async () => {
    try {
      await logout();
      alert("로그아웃 성공");
    } catch (error) {
      alert(error.message);
    } finally {
      router.push("/");
    }
  };

  return (
    <>
      <div>
        <h1>홈페이지</h1>
        <div>
          <button onClick={() => router.push("/auth/login")}>로그인</button>

          <button onClick={() => router.push("/auth/register")}>
            회원가입
          </button>

          <button onClick={() => handleLogout()}>로그아웃</button>

          <button onClick={() => router.push("/qna")}>QnA</button>
        </div>
      </div>
    </>
  );
}
