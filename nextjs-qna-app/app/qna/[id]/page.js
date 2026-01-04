"use client";

import { use, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { retrieveQna } from "@/app/api/qna";
import CommentForm from "./component/CommentForm";

export default function QnaDetailPage({ params }) {
    const router = useRouter();
    const { id } = use(params);

    const [comments, setComments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [forbidden, setForbidden] = useState(false);
    const [error, setError] = useState(null);

    const loadQna = async () => {
        try {
            setLoading(true);
            const data = await retrieveQna(id);
            setComments(data);
        } catch (e) {
            if (e?.status === 403) {
                setForbidden(true);
            } else {
                setError("존재하지 않는 질문입니다.");
            }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (id) loadQna();
    }, [id]);

    const goBack = () => router.push("/qna");
    const goHome = () => router.push("/");

    if (loading)
        return (
            <div className="min-h-screen flex items-center justify-center bg-slate-50">
                <p className="text-slate-600">불러오는 중...</p>
            </div>
        );

    if (forbidden || error)
        return (
            <div className="min-h-screen flex items-center justify-center bg-slate-50">
                <p className="text-slate-600">
                    {forbidden ? "이 질문을 열람할 권한이 없습니다." : error}
                </p>
            </div>
        );

    if (!comments || comments.length === 0)
        return (
            <div className="min-h-screen flex items-center justify-center bg-slate-50">
                <p className="text-slate-600">질문 정보가 없습니다.</p>
            </div>
        );

    const question = comments[0];
    const answers = comments.slice(1);

    return (
        <div className="min-h-screen bg-slate-50 px-4 py-10">
            <div className="mx-auto max-w-3xl space-y-6">
                {/* 질문 카드 */}
                <div className="bg-white border border-slate-200 rounded-lg shadow-sm p-6 space-y-4">
                    <h2 className="text-2xl font-bold text-slate-800">질문</h2>

                    <div className="text-sm text-slate-500">
                        작성자: <span className="font-medium">{question.username}</span>
                    </div>

                    <p className="text-slate-700 leading-relaxed">
                        {question.comment}
                    </p>
                </div>

                {/* 답변 카드 */}
                <div className="bg-white border border-slate-200 rounded-lg shadow-sm p-6 space-y-4">
                    <h3 className="text-xl font-semibold text-slate-800">답변</h3>

                    {answers.length === 0 && (
                        <p className="text-slate-500">아직 답변이 없습니다.</p>
                    )}

                    <ul className="space-y-4">
                        {answers.map((answer) => (
                            <li
                                key={answer.id}
                                className="border border-slate-200 rounded-lg p-4 bg-slate-50"
                            >
                                <p className="text-slate-700 mb-2">
                                    {answer.comment}
                                </p>
                                <div className="text-sm text-slate-500">
                                    작성자:{" "}
                                    <span className="font-medium">
                    {answer.user.username}
                  </span>
                                </div>
                            </li>
                        ))}
                    </ul>
                </div>

                {/* 댓글 작성 */}
                <div className="bg-white border border-slate-200 rounded-lg shadow-sm p-6">
                    <CommentForm qnaId={id} onSuccess={loadQna} />
                </div>

                {/* 하단 버튼 */}
                <div className="flex justify-between">
                    <button
                        onClick={goBack}
                        className="px-4 py-2 rounded-lg bg-slate-100 text-slate-700 hover:bg-slate-200 transition"
                    >
                        뒤로가기
                    </button>

                    <button
                        onClick={goHome}
                        className="px-4 py-2 rounded-lg bg-indigo-600 text-white hover:bg-indigo-700 transition"
                    >
                        홈으로
                    </button>
                </div>
            </div>
        </div>
    );
}
