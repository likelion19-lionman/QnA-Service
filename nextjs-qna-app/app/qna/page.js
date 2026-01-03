'use client';

import { useEffect, useState } from 'react';
import { retrieveQnas, deleteQna } from '@/app/api/qna';
import QnaList from './component/QnaList';

export default function QnaHome() {
    const [qnas, setQnas] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const loadQnas = async () => {
        try {
            setLoading(true);
            const res = await retrieveQnas(0, 10); // page=0, size=10
            setQnas(res.content ?? res);
        } catch (e) {
            setError('질문 목록을 불러오지 못했습니다.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadQnas();
    }, []);

    const handleDelete = async (qnaId) => {
        const ok = window.confirm('정말 삭제하시겠습니까?');
        if (!ok) return;

        try {
            await deleteQna(qnaId);
            loadQnas();
        } catch (e) {
            alert('삭제에 실패했습니다.');
        }
    };

    /* 로딩 상태 */
    if (loading)
        return (
            <div className="min-h-screen bg-slate-50 flex items-center justify-center px-4">
                <div className="bg-white border border-slate-200 rounded-lg shadow-sm p-6 text-slate-600">
                    로딩 중...
                </div>
            </div>
        );

    /* 에러 상태 */
    if (error)
        return (
            <div className="min-h-screen bg-slate-50 flex items-center justify-center px-4">
                <div className="bg-white border border-slate-200 rounded-lg shadow-sm p-6 text-red-600">
                    {error}
                </div>
            </div>
        );

    return (
        <div className="min-h-screen bg-slate-50 px-4 py-10">
            <div className="mx-auto max-w-3xl space-y-6">
                {/* 페이지 제목 */}
                <h1 className="text-3xl font-bold text-slate-800">
                    나의 문의 사항
                </h1>

                {/* QnA 리스트 */}
                <QnaList
                    qnas={qnas}
                    onDelete={handleDelete}
                />
            </div>
        </div>
    );
}
