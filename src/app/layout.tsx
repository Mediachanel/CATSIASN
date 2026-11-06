import type { Metadata } from "next";
import "./globals.css";
export const metadata: Metadata = { title: "CAT Nusantara", description: "Persiapan SKD yang lebih terarah." };
export default function RootLayout({children}:{children:React.ReactNode}) { return <html lang="id"><body>{children}</body></html> }
