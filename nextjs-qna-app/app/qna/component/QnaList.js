"use client";

import { useRouter } from "next/navigation";
import Link from "next/link";

export default function QnaList({ qnas, onDelete }) {
    const router = useRouter();

    const goHome = () => {
        router.push("/");
    };

    return (
        <div className="min-h-screen bg-slate-50 px-4 py-10">
            <div className="mx-auto max-w-3xl space-y-6">
                {/* 헤더 */}
                <div className="flex items-center justify-between">
                    <h1 className="text-3xl font-bold text-slate-800">QnA</h1>

                    <Link href="/qna/create">
                        <button
                            type="button"
                            className="px-4 py-2 rounded-lg bg-indigo-600 text-white font-medium hover:bg-indigo-700 transition"
                        >
                            글작성하기
                        </button>
                    </Link>
                </div>

                {/* 빈 상태 */}
                {qnas.length === 0 && (
                    <div className="bg-white border border-slate-200 rounded-lg shadow-sm p-6 text-center text-slate-500">
                        질문이 없습니다.
                    </div>
                )}

                {/* 리스트 */}
                <ul className="space-y-4">
                    {qnas.map((qna) => (
                        <li
                            key={qna.id}
                            className="bg-white border border-slate-200 rounded-lg shadow-sm p-5 flex items-center justify-between"
                        >
                            {/* 제목 → 상세 */}
                            <Link
                                href={`/qna/${qna.id}`}
                                className="text-lg font-medium text-slate-800 hover:underline"
                            >
                                {qna.title}
                            </Link>

                            {/* 액션 */}
                            <button
                                type="button"
                                onClick={() => onDelete(qna.id)}
                                className="px-3 py-1.5 rounded-lg text-sm font-medium bg-red-100 text-red-600 hover:bg-red-200 transition"
                            >
                                삭제
                            </button>
                        </li>
                    ))}
                </ul>

                {/* 하단 버튼 */}
                <div className="flex justify-between pt-4">
                    <button
                        onClick={goHome}
                        className="px-4 py-2 rounded-lg bg-slate-100 text-slate-700 hover:bg-slate-200 transition"
                    >
                        홈으로
                    </button>
                </div>
            </div>
        </div>
    );
}
