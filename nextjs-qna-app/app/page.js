"use client";

import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import { logout } from "@/app/api/auth";

// JWT 토큰 디코딩 함수 (라이브러리 다운 후 사용하는 방법도 있음)
function decodeJWT(token) {
  try {
    const base64Url = token.split(".")[1]; // payload 부분
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    );
    return JSON.parse(jsonPayload);
  } catch (e) {
    console.error("JWT 토큰 디코딩 오류:", e);
    return null;
  }
}

export default function HomePage() {
  const router = useRouter();

  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState("");

  // 컴포넌트 마운트 시 로그인 상태 확인
  useEffect(() => {
    const refreshToken = localStorage.getItem("refreshToken");

    if (refreshToken) {
      const decoded = decodeJWT(refreshToken);
      if (decoded && decoded.username) {
        setIsLoggedIn(true);
        setUsername(decoded.username);
      }
    }
  }, []);

  const handleLogout = async () => {
    try {
      await logout();
      setIsLoggedIn(false);
      setUsername("");
      localStorage.removeItem("refreshToken");
      alert("로그아웃 성공");
    } catch (error) {
      alert(error.message);
    } finally {
      router.push("/");
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center px-4">
      <div className="w-full max-w-md space-y-8">
        <div className="text-center space-y-2">
          <h1 className="text-4xl font-bold text-slate-800">QnA 서비스</h1>
          <p className="text-slate-600">질문과 답변을 주고받는 공간</p>
        </div>

        <div className="bg-white rounded-lg shadow-sm border border-slate-200 p-6 space-y-4">
          {/* 비로그인 상태일 때 */}
          {!isLoggedIn && (
            <>
              <button
                onClick={() => router.push("/qna")}
                className="w-full px-4 py-3 bg-emerald-600 text-white rounded-lg font-medium hover:bg-emerald-700 transition-colors duration-200 shadow-sm"
              >
                QnA 보기
              </button>

              <button
                onClick={() => router.push("/auth/login")}
                className="w-full px-4 py-3 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition-colors duration-200 shadow-sm"
              >
                로그인
              </button>

              <button
                onClick={() => router.push("/auth/register")}
                className="w-full px-4 py-3 bg-slate-100 text-slate-700 rounded-lg font-medium hover:bg-slate-200 transition-colors duration-200"
              >
                회원가입
              </button>
            </>
          )}

          {/* 로그인 상태일 때 */}
          {isLoggedIn && (
            <>
              <button
                onClick={() => router.push("/qna")}
                className="w-full px-4 py-3 bg-emerald-600 text-white rounded-lg font-medium hover:bg-emerald-700 transition-colors duration-200 shadow-sm"
              >
                QnA 보기
              </button>

              <button
                onClick={handleLogout}
                className="w-full px-4 py-3 bg-slate-50 text-slate-500 rounded-lg font-medium hover:bg-slate-100 transition-colors duration-200 border border-slate-200"
              >
                로그아웃
              </button>

              <div className="pt-2 border-t border-slate-200 text-center text-slate-600">
                현재 접속 중인 사용자:{" "}
                <span className="font-medium">{username}</span>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}
