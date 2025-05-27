export interface StatCardProps {
  title: string;
  value: number | string;
  icon: React.ComponentType<{
    className?: string;
    style?: React.CSSProperties;
  }>;
  color: string;
  change?: {
    value: number;
    isPositive: boolean;
  };
}
