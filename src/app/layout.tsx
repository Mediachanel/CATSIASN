import type { Metadata } from "next";
import "./globals.css";
export const metadata: Metadata = { title: "CAT Nusantara", description: "Persiapan SKD yang lebih terarah.", icons: { icon: "/cpns_launcher_icon_1794011079614.jpg", apple: "/cpns_launcher_icon_1794011079614.jpg" } };
export default function RootLayout({children}:{children:React.ReactNode}) { return <html lang="id"><body>{children}</body></html> }
