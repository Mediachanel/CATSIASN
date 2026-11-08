import { getAuthenticatedUser } from "../../../../../../lib/auth/mobile";

export async function GET(request: Request) {
  const user = await getAuthenticatedUser(request);
  if (!user) {
    return Response.json(
      { success: false, error: "UNAUTHORIZED", message: "Sesi login tidak valid atau sudah kedaluwarsa." },
      { status: 401 }
    );
  }

  return Response.json({
    success: true,
    user: {
      id: user.id,
      email: user.email,
      name: user.name,
      role: user.role,
    },
  });
}
