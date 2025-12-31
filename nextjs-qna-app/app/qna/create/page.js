'use client'

import { useState } from 'react';
import { query } from '@/app/api/qna';

export default function CreateQnaPage() {
    const [title, setTitle] = useState('');
    const [comment, setComment] = useState('');
    const [submitting, setSubmitting] = useState(false);
    
    const submit = async () => {
        if (!title.trim()) return alert('제목을 입력하세요.');
        if (!comment.trim()) return alert('내용을 입력하세요.');

        try {
            if (!confirm("등록 후에는 수정이 불가합니다. 게시하시겠습니까?")) {
                console.log("게시 취소됨");
                return;
            }
            setSubmitting(true);
            await query(title, comment);
            alert('게시글이 등록되었습니다.');
            setTitle('');
            setComment('');
        } catch (e) {
            alert(e?.message ?? '게시글 등록 실패');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div>
            <input
                placeholder="제목"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
            /><br/>

            <textarea
            placeholder="댓글을 입력하세요"
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            /><br />

            <button type="button" onClick={submit}>게시</button>
        </div>
    )

}