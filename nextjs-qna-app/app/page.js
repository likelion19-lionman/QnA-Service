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
    <div className="min-h-screen flex flex-col items-center justify-center px-4">
      <div className="w-full max-w-md space-y-8">
        <div className="text-center space-y-2">
          <h1 className="text-4xl font-bold text-slate-800">QnA 서비스</h1>
          <p className="text-slate-600">질문과 답변을 주고받는 공간</p>
        </div>

        <div className="bg-white rounded-lg shadow-sm border border-slate-200 p-6 space-y-4">
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

          <button
            onClick={() => handleLogout()}
            className="w-full px-4 py-3 bg-slate-50 text-slate-500 rounded-lg font-medium hover:bg-slate-100 transition-colors duration-200 border border-slate-200"
          >
            로그아웃
          </button>

          <div className="pt-2 border-t border-slate-200">
            <button
              onClick={() => router.push("/qna")}
              className="w-full px-4 py-3 bg-emerald-600 text-white rounded-lg font-medium hover:bg-emerald-700 transition-colors duration-200 shadow-sm"
            >
              QnA 보기
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
