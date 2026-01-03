'use client';

import Link from 'next/link';

export default function QnaList({ qnas, onDelete }) {
    return (
        <div>
            <h1>QnA List</h1>

            {qnas.length === 0 && <p>질문이 없습니다.</p>}

            <ul>
                {qnas.map((qna) => (
                    <li key={qna.id} style={{ marginBottom: '12px' }}>
                        {/* 카드 클릭 → 상세 */}
                        <Link href={`/qna/${qna.id}`}>
                            <strong>{qna.title}</strong>
                        </Link>

                        <div>
                            <button
                                type="button"
                                onClick={() => onDelete(qna.id)}
                            >
                                삭제
                            </button>
                        </div>
                    </li>
                ))}
            </ul>

            {/* 문의하기 */}
            <Link href="/qna/create">
                <button type="button">문의하기</button>
            </Link>
        </div>
    );
}
