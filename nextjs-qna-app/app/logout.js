// const API_BASE_URL = "http://localhost:8080/api/auth/logout";

// export async function logout() {
//   const refreshToken = localStorage.getItem("refreshToken");

//   // refreshToken이 없으면 이미 로그아웃 상태
//   if (!refreshToken) {
//     window.location.href = "/auth/login"; // 로그인 페이지로 이동
//     return;
//   }

//   try {
//     // 로그아웃 요청
//     await fetch(API_BASE_URL, {
//       method: "POST",
//       headers: {
//         "Content-Type": "application/json",
//       },
//       body: JSON.stringify({ refreshToken }),
//       credentials: "include",
//     });
//   } catch (error) {
//     console.error("로그아웃 실패:", error);
//   } finally {
//     // 성공/실패 여부와 상관없이 refreshToken 제거 후 로그인 페이지로 이동
//     localStorage.removeItem("refreshToken");
//     window.location.href = "/auth/login";
//   }
// }
