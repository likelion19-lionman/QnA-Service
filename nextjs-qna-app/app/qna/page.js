'use client';

import { useEffect, useState } from 'react';
import { retrieveQnas, deleteQna } from '@/app/api/qna';
import QnaList from './component/QnaList';

export default function QnaHome() {

    const [qnas, setQnas] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const loadQnas = async () => {
        console.log("loadqna")
        try {
            setLoading(true);
            console.log("loadqna2")

            const res = await retrieveQnas(0, 10); // 기본 page=0, size=10
            console.log(res)
            setQnas(res.content ?? res);
        } catch (e) {
            setError('질문 목록을 불러오지 못했습니다.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadQnas();
        console.log("dd")
    }, []);

    const handleDelete = async (qnaId) => {
        const ok = window.confirm('정말 삭제하시겠습니까?');
        if (!ok) return;

        try {
            await deleteQna(qnaId);
            loadQnas(); // 삭제 후 재조회
        } catch (e) {
            alert('삭제에 실패했습니다.');
        }
    };

    if (loading) return <p>로딩 중...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div>
            <h1>나의 문의 사항</h1>

            <QnaList
                qnas={qnas}
                onDelete={handleDelete}
            />
        </div>
    );
}
