'use client';

import { useState } from 'react';
import { addComment } from '@/app/api/qna';

export default function CommentForm({ qnaId }) {
    const [comment, setComment] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const submit = async (e) => {
        e.preventDefault();
        const trimmed = comment.replace(/^[\s"']+|[\s"']+$/g, '').trim();
        if (!trimmed) return;

        try {
            setSubmitting(true);
            const res = await addComment(qnaId, trimmed);
            setComment('');
            // if (onSuccess) await onSuccess();
        } catch (e) {
            alert('댓글 작성 실패');
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="min-h-screen bg-slate-50 py-8 px-4">
            <div className="max-w-3xl mx-auto">
                <div className="bg-white rounded-lg shadow-sm border border-slate-200 p-6 space-y-4">
                    <h2 className="text-xl font-bold text-slate-800">댓글 작성</h2>
                    <div className="space-y-3">
                        <textarea
                            placeholder="댓글을 입력하세요"
                            value={comment}
                            onChange={(e) => setComment(e.target.value)}
                            disabled={submitting}
                            rows={6}
                            className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all resize-none disabled:bg-slate-50 disabled:cursor-not-allowed"
                        />
                        <button 
                            type="button" 
                            onClick={(e) => submit(e)} 
                            disabled={submitting}
                            className="px-6 py-2 bg-indigo-600 text-white rounded-lg font-medium hover:bg-indigo-700 transition-colors duration-200 shadow-sm disabled:bg-slate-300 disabled:cursor-not-allowed"
                        >
                            {submitting ? '게시 중...' : '게시'}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
