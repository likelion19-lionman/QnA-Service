'use client';

export default function CommentForm({ qnaId, onSuccess }) {
    return <></>;
    // const [comment, setComment] = useState('');
    // const [submitting, setSubmitting] = useState(false);

    // const submit = async () => {
    //     const trimmed = comment.trim();
    //     if (!trimmed) return;

    //     try {
    //         setSubmitting(true);
    //         await qna(qnaId, trimmed);          
    //         setComment('');
    //         if (onSuccess) await onSuccess();   
    //     } catch (e) {
    //         alert(e?.message ?? '댓글 작성 실패');
    //     } finally {
    //         setSubmitting(false);
    //     }
    // };

    // return (
    //     <div>
    //         <textarea
    //         placeholder="댓글을 입력하세요"
    //         value={comment}
    //         onChange={(e) => setComment(e.target.value)}
    //         disabled={submitting}
    //         />
    //         <br />
    //         <button type="button" onClick={submit} disabled={submitting}>
    //         게시
    //         </button>
    //     </div>
    // );
}
